package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.balance.PeriodTotals
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.settings.DefaultUserSettings
import com.jorgelobo.koobe.domain.model.shortcut.Shortcut
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import com.jorgelobo.koobe.domain.usecase.category.GetAllCategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetAllShortcutsUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetAllSubcategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.GetTransactionPeriodTotalsUseCase
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.utils.date.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTransactionPeriodTotals: GetTransactionPeriodTotalsUseCase,
    getAllBudgets: GetAllBudgetsUseCase,
    getAllShortcuts: GetAllShortcutsUseCase,
    getAllCategories: GetAllCategoriesUseCase,
    getAllSubcategories: GetAllSubcategoriesUseCase,
    getUserSettingsUseCase: GetUserSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    private val _events = MutableSharedFlow<DashboardEvent>()
    val events = _events.asSharedFlow()

    private val currentDate = DateUtils.currentDate

    private val userSettings = getUserSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DefaultUserSettings
        )

    private val budgetItemsFlow =
        combine(
            getAllBudgets(),
            getAllCategories(),
            getAllSubcategories()
        ) { budgets, categories, subcategories ->

            val categoriesById = categories.associateBy { it.id }
            val subcategoriesById = subcategories.associateBy { it.id }

            budgets.mapNotNull { budget ->

                val category = categoriesById[budget.categoryId]
                val subcategory = subcategoriesById[budget.subcategoryId]

                if (category != null && subcategory != null) {
                    BudgetUiModel(
                        budget = budget,
                        category = category,
                        subcategory = subcategory
                    )
                } else {
                    null
                }
            }
        }

    private val shortcutItemsFlow =
        combine(
            getAllShortcuts(),
            getAllCategories()
        ) { shortcuts, categories ->

            val categoriesById = categories.associateBy { it.id }

            shortcuts
                .sortedWith(
                    compareByDescending<Shortcut> { it.usageCount }
                        .thenBy { it.name.lowercase() }
                )
                .take(3)
                .mapNotNull { shortcut ->
                    categoriesById[shortcut.categoryId]?.let { category ->
                        ShortcutUiModel(
                            shortcut = shortcut,
                            category = category
                        )
                    }
                }
        }

    init {
        observeUserSettings()
        observeDashboardData()
        observeBalances()
    }

    private fun observeUserSettings() {
        viewModelScope.launch {
            userSettings.collect { settings ->
                _uiState.update { state ->
                    state.copy(
                        currencyType = settings.currency,
                        startOfWeek = settings.startOfWeek
                    )
                }
            }
        }
    }

    private fun observeDashboardData() {
        viewModelScope.launch {
            combine(
                budgetItemsFlow,
                shortcutItemsFlow
            ) { budgetItems, shortcutItems ->
                DashboardData(
                    budgetItems = budgetItems,
                    shortcutItems = shortcutItems
                )
            }.collect { data ->
                _uiState.update {
                    it.copy(
                        budgetItems = data.budgetItems,
                        shortcutItems = data.shortcutItems
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeBalances() {
        viewModelScope.launch {
            userSettings
                .flatMapLatest { settings ->
                    val monthlyRange = DateUtils.getPeriodRange(
                        date = currentDate,
                        periodType = PeriodType.MONTHLY
                    )

                    val dailyRange = DateUtils.getPeriodRange(
                        date = currentDate,
                        periodType = PeriodType.DAILY
                    )

                    val weeklyRange = DateUtils.getPeriodRange(
                        date = currentDate,
                        periodType = PeriodType.WEEKLY,
                        startOfWeek = settings.startOfWeek
                    )

                    combine(
                        getTransactionPeriodTotals(),
                        getTransactionPeriodTotals(monthlyRange.first, monthlyRange.second),
                        getTransactionPeriodTotals(dailyRange.first, dailyRange.second),
                        getTransactionPeriodTotals(weeklyRange.first, weeklyRange.second)
                    ) { overall, monthly, daily, weekly ->
                        DashboardBalances(
                            overall = overall,
                            monthly = monthly,
                            daily = daily,
                            weekly = weekly
                        )
                    }
                }
                .collect { balances ->
                    _uiState.update {
                        it.copy(
                            overallBalance = balances.overall.balance,
                            income = balances.monthly.income,
                            expenses = balances.monthly.expenses,
                            dailyIncome = balances.daily.income,
                            dailyExpenses = balances.daily.expenses,
                            weeklyIncome = balances.weekly.income,
                            weeklyExpenses = balances.weekly.expenses
                        )
                    }
                }
        }
    }

    fun onAddTransactionClick(type: TransactionType) {
        val route = Route.CategorySelector.create(
            CategorySelectorConfig(
                mode = CategorySelectorMode.CREATE_TRANSACTION,
                target = CategorySelectorTarget.TRANSACTION_EDITOR,
                initialTransactionType = type
            )
        )

        navigateTo(route)
    }

    fun onBudgetItemClick(item: BudgetUiModel) {
        val route = Route.BudgetEditor.create(
            BudgetEditorConfig(budgetId = item.budget.id)
        )

        navigateTo(route)
    }

    fun onBudgetActionClick(hasBudgets: Boolean) {
        val route = if (hasBudgets) {
            Route.BudgetEditor.route
        } else {
            Route.BudgetEditor.create(BudgetEditorConfig(budgetId = null))
        }

        navigateTo(route)
    }

    fun onShortcutItemClick(item: ShortcutUiModel) {
        val route = Route.ShortcutEditor.create(
            ShortcutEditorConfig.Edit(shortcutId = item.shortcut.id)
        )

        navigateTo(route)
    }

    fun onShortcutActionClick(hasShortcuts: Boolean) {
        val route = if (hasShortcuts) {
            Route.ShortcutManager.route
        } else {
            Route.CategorySelector.create(
                CategorySelectorConfig(
                    mode = CategorySelectorMode.CREATE_SHORTCUT,
                    target = CategorySelectorTarget.SHORTCUT_EDITOR,
                    initialTransactionType = TransactionType.EXPENSE
                )
            )
        }

        navigateTo(route)
    }

    private fun navigateTo(route: String) {
        emitEvent(DashboardEvent.NavigateTo(route))
    }

    private fun emitEvent(event: DashboardEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    private data class DashboardBalances(
        val overall: PeriodTotals,
        val monthly: PeriodTotals,
        val daily: PeriodTotals,
        val weekly: PeriodTotals
    )

    private data class DashboardData(
        val budgetItems: List<BudgetUiModel>,
        val shortcutItems: List<ShortcutUiModel>
    )
}
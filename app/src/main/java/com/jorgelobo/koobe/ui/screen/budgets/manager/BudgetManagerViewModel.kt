package com.jorgelobo.koobe.ui.screen.budgets.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.settings.DefaultUserSettings
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import com.jorgelobo.koobe.domain.usecase.category.GetAllCategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetAllSubcategoriesUseCase
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.budgets.manager.model.PeriodicBudgetsUiModel
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetManagerViewModel @Inject constructor(
    getAllBudgets: GetAllBudgetsUseCase,
    getAllCategories: GetAllCategoriesUseCase,
    getAllSubcategories: GetAllSubcategoriesUseCase,
    getUserSettings: GetUserSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetManagerUiState())
    val uiState: StateFlow<BudgetManagerUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BudgetManagerEvent>()
    val events: SharedFlow<BudgetManagerEvent> = _events.asSharedFlow()

    private val userSettings = getUserSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DefaultUserSettings
        )

    private val budgetItemsFlow = combine(
        getAllBudgets(),
        getAllCategories(),
        getAllSubcategories()
    ) { budgets, categories, subcategories ->

        val categoriesById = categories.associateBy { it.id }
        val subcategoriesById = subcategories.associateBy { it.id }

        budgets.mapNotNull { budget ->
            val category = categoriesById[budget.categoryId]
            val subcategory = budget.subcategoryId?.let(subcategoriesById::get)

            if (category == null) {
                null
            } else {
                BudgetUiModel.from(
                    budget = budget,
                    category = category,
                    subcategory = subcategory
                )
            }
        }
    }

    private val periodicBudgetsFlow = combine(
        budgetItemsFlow,
        userSettings
    ) { budgets, settings ->
        budgets
            .groupBy { it.budget.period }
            .map { (periodType, budgets) ->
                PeriodicBudgetsUiModel(
                    periodType = periodType,
                    currencyType = settings.currency,
                    budgets = budgets,
                    totalLimit = budgets.sumOf { it.budget.limitAmount },
                    totalSpent = budgets.sumOf { it.budget.spentAmount }
                )
            }
            .sortedBy { it.periodType }
    }

    init {
        observeUserSettings()
        observeBudgets()
    }

    private fun observeUserSettings() {
        viewModelScope.launch {
            userSettings.collect { settings ->
                updateState { copy(currencyType = settings.currency) }
            }
        }
    }

    private fun observeBudgets() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            periodicBudgetsFlow.collect { periodicBudgets ->
                updateState {
                    copy(
                        periodicBudgets = periodicBudgets,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onAddBudgetClick() {
        val route = Route.CategorySelector.create(
            CategorySelectorConfig(
                mode = CategorySelectorMode.CREATE_BUDGET,
                target = CategorySelectorTarget.BUDGET_EDITOR,
                initialTransactionType = TransactionType.EXPENSE
            )
        )
        navigateTo(route)
    }

    private fun navigateTo(route: String) {
        emitEvent(BudgetManagerEvent.NavigateTo(route))
    }

    private fun navigateBack() {
        emitEvent(BudgetManagerEvent.NavigateBack)
    }

    private fun emitEvent(event: BudgetManagerEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun updateState(reducer: BudgetManagerUiState.() -> BudgetManagerUiState) {
        _uiState.update { it.reducer() }
    }
}
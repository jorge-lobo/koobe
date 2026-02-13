package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.BudgetRepository
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val shortcutRepository: ShortcutRepository,
    private val categoryRepository: CategoryRepository,
    private val subcategoryRepository: SubcategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    private val _events = MutableSharedFlow<DashboardEvent>()
    val events = _events.asSharedFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val budgets = budgetRepository.getAllBudgets().first()
                val shortcuts = shortcutRepository.getAllShortcuts().first()
                val categories = categoryRepository.getAllCategories().first()
                val subcategories = subcategoryRepository.getAllSubcategories().first()

                val budgetItems = budgets.map { budget ->
                    BudgetUiModel(
                        budget = budget,
                        category = categories.first { it.id == budget.categoryId },
                        subcategory = subcategories.first { it.id == budget.subcategoryId }
                    )
                }

                val shortcutItems = shortcuts.map { shortcut ->
                    ShortcutUiModel(
                        shortcut = shortcut,
                        category = categories.first { it.id == shortcut.categoryId }
                    )
                }

                _uiState.update {
                    it.copy(
                        budgetItems = budgetItems,
                        shortcutItems = shortcutItems,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Unknown error"
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
            ShortcutEditorConfig(shortcutId = item.shortcut.id)
        )

        navigateTo(route)
    }

    fun onShortcutActionClick(hasShortcuts: Boolean) {
        val route = if (hasShortcuts) {
            Route.ShortcutManager.route
        } else {
            Route.ShortcutEditor.create(ShortcutEditorConfig(shortcutId = null))
        }

        navigateTo(route)
    }

    private fun navigateTo(route: String) {
        emitEvent(DashboardEvent.NavigateTo(route))
    }

    private fun emitEvent(event: DashboardEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}
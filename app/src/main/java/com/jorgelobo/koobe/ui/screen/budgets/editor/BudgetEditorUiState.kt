package com.jorgelobo.koobe.ui.screen.budgets.editor

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

/**
 * Represents the UI state for the Budget Editor screen.
 *
 * This state manages the data required to create or edit a budget, including category selection,
 * limit amounts, frequency settings, and the visibility of various confirmation dialogs.
 * It also tracks changes against an initial snapshot to determine if the state is dirty and
 * eligible for saving.
 *
 * @property config The configuration determining if the editor is in create or edit mode.
 * @property budget The budget entity being modified.
 * @property category The selected category for the budget.
 * @property subcategory The selected subcategory for the budget.
 * @property inputState The current state of form inputs (e.g., focused, error).
 * @property language The current application language for formatting purposes.
 * @property currencyType The currency used for the budget amounts.
 * @property isRepeat Indicates if the budget should automatically repeat in the next period.
 * @property period The frequency of the budget (e.g., Monthly, Weekly).
 * @property limitAmountInput The raw string value of the limit amount as typed by the user.
 * @property limitAmount The parsed double value of the budget's limit.
 * @property spentAmount The current amount already spent within this budget's scope.
 * @property projectedAmount The estimated total spending by the end of the period.
 * @property dailyAverage The calculated daily average spend allowed to stay within budget.
 * @property isLoading Indicates if the screen is currently fetching data.
 * @property isSaving Indicates if a save operation is in progress.
 * @property isDeleting Indicates if a deletion operation is in progress.
 * @property errorMessage Holds an error message to display in case of failure.
 * @property budgetInitialSnapshot A snapshot of the budget's properties at the start of the session to detect changes.
 * @property discardDialog State for the dialog shown when trying to leave with unsaved changes.
 * @property deleteDialog State for the dialog shown when confirming budget deletion.
 * @property duplicateBudgetDialog State for the dialog shown when a budget for the same category/subcategory already exists.
 * @property existingBudgets A list of budgets that might conflict with the current selection.
 * @property isExistingBudgetsDialogVisible Controls the visibility of the conflict/existing budgets list.
 */
data class BudgetEditorUiState(
    val config: BudgetEditorConfig? = null,
    val budget: Budget,
    val category: Category,
    val subcategory: Subcategory,
    val inputState: InputState,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val currencyType: CurrencyType = CurrencyType.EUR,
    val isRepeat: Boolean = false,
    val period: PeriodType = PeriodType.MONTHLY,
    val limitAmountInput: String = "0",
    val limitAmount: Double = 0.0,
    val spentAmount: Double = 0.0,
    val projectedAmount: Double = 0.0,
    val dailyAverage: Double = 0.0,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val budgetInitialSnapshot: BudgetInitialSnapshot,
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val duplicateBudgetDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val existingBudgets: List<BudgetUiModel> = emptyList(),
    val isExistingBudgetsDialogVisible: Boolean = false
) {

    // Indicates whether the budget data is valid for saving.
    val isValid: Boolean
        get() = limitAmount > 0

    /**
     * Indicates whether the current state allows saving the budget.
     *
     * Returns true if the inputs are valid and, in the case of editing an existing budget, if any
     * changes have been made compared to the initial snapshot.
     */
    val isSaveEnabled: Boolean
        get() {
            val config = config ?: return false

            if (!isValid) return false

            return when (config) {
                is BudgetEditorConfig.Create -> true
                is BudgetEditorConfig.Edit -> {
                    val initial = budgetInitialSnapshot

                    category.id != initial.categoryId ||
                            subcategory.id != initial.subcategoryId ||
                            isRepeat != initial.isRepeat ||
                            period != initial.period ||
                            limitAmount != initial.limitAmount
                }
            }
        }

    // Returns the screen title string resource based on the current mode.
    fun headlineRes(): Int =
        when (config) {
            is BudgetEditorConfig.Create -> R.string.headline_budget_creator
            else -> R.string.headline_budget_editor
        }

    companion object {

        fun initialEmpty(): BudgetEditorUiState {
            val emptyBudget = Budget.empty()
            val emptyCategory = Category.empty()
            val emptySubcategory = Subcategory.empty()

            return BudgetEditorUiState(
                budget = emptyBudget,
                category = emptyCategory,
                subcategory = emptySubcategory,
                inputState = InputState.DEFAULT,
                isLoading = true,
                budgetInitialSnapshot = BudgetInitialSnapshot(
                    categoryId = emptyCategory.id,
                    subcategoryId = emptySubcategory.id,
                    isRepeat = false,
                    period = PeriodType.MONTHLY,
                    limitAmount = 0.0
                )
            )
        }

        fun initial(
            config: BudgetEditorConfig,
            budget: Budget,
            category: Category,
            subcategory: Subcategory,
            isRepeat: Boolean,
            period: PeriodType
        ): BudgetEditorUiState {
            return BudgetEditorUiState(
                config = config,
                budget = budget,
                category = category,
                subcategory = subcategory,
                inputState = InputState.DEFAULT,
                isRepeat = isRepeat,
                period = period,
                budgetInitialSnapshot = BudgetInitialSnapshot(
                    categoryId = category.id,
                    subcategoryId = subcategory.id,
                    isRepeat = isRepeat,
                    period = period,
                    limitAmount = 0.0
                )
            )
        }

        fun initialFromBudget(
            config: BudgetEditorConfig,
            budget: Budget,
            category: Category,
            subcategory: Subcategory,
            isRepeat: Boolean,
            period: PeriodType
        ): BudgetEditorUiState {
            return BudgetEditorUiState(
                config = config,
                budget = budget,
                category = category,
                subcategory = subcategory,
                inputState = InputState.DEFAULT,
                isRepeat = isRepeat,
                period = period,
                limitAmountInput = budget.limitAmount.toString(),
                limitAmount = budget.limitAmount,
                spentAmount = budget.spentAmount,
                projectedAmount = budget.projectedAmount,
                dailyAverage = budget.dailyAverage,
                isLoading = false,
                budgetInitialSnapshot = BudgetInitialSnapshot(
                    categoryId = category.id,
                    subcategoryId = subcategory.id,
                    isRepeat = isRepeat,
                    period = period,
                    limitAmount = budget.limitAmount
                )
            )
        }
    }
}

/**
 * A snapshot representing the initial state of a budget when the editor is first loaded.
 *
 * This data class is used to track the original values of a budget's key properties. It serves
 * as a baseline to detect if the user has made any changes (dirty checking), which in turn
 * determines whether the "Save" action should be enabled or if a "Discard Changes" warning
 * should be displayed.
 *
 * @property categoryId The ID of the category initially assigned to the budget.
 * @property subcategoryId The ID of the subcategory initially assigned to the budget.
 * @property isRepeat The initial value of whether the budget was set to automatically repeat.
 * @property period The initial frequency/period (e.g., Monthly, Weekly) of the budget.
 * @property limitAmount The initial spending limit amount assigned to the budget.
 */
data class BudgetInitialSnapshot(
    val categoryId: Int,
    val subcategoryId: Int,
    val isRepeat: Boolean,
    val period: PeriodType,
    val limitAmount: Double
)
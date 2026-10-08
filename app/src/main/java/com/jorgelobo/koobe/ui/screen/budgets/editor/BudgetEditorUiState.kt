package com.jorgelobo.koobe.ui.screen.budgets.editor

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

data class BudgetEditorUiState(
    val config: BudgetEditorConfig? = null,
    val category: Category? = null,
    val subcategory: Subcategory? = null,
    val inputState: InputState,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val isRepeat: Boolean = false,
    val period: PeriodType = PeriodType.MONTHLY,
    val limitAmountInput: String = "0",
    val limitAmount: Double = 0.0,
    val spentAmount: Double = 0.0,
    val projectedAmount: Double = 0.0,
    val dailyAverage: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val budgetInitialSnapshot: BudgetInitialSnapshot,
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState()
) {

    val isValid: Boolean
        get() = category != null &&
                subcategory != null &&
                limitAmount > 0

    val isSaveEnabled: Boolean
        get() {
            val config = config ?: return false

            if (!isValid) return false

            if (!config.isEditMode) return true

            val initial = budgetInitialSnapshot

            return category?.id != initial.categoryId ||
                    subcategory?.id != initial.subcategoryId ||
                    isRepeat != initial.isRepeat ||
                    period != initial.period ||
                    limitAmount != initial.limitAmount
        }

    fun headlineRes(): Int =
        if (config?.isEditMode == true) {
            R.string.headline_budget_editor
        } else {
            R.string.headline_budget_creator
        }

    companion object {

        fun initialEmpty(): BudgetEditorUiState {
            val emptyCategory = Category.empty()
            val emptySubcategory = Subcategory.empty()

            return BudgetEditorUiState(
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
            category: Category,
            subcategory: Subcategory,
            language: AppLanguage,
            isRepeat: Boolean,
            period: PeriodType,
            limitAmount: Double
        ): BudgetEditorUiState {
            return BudgetEditorUiState(
                config = config,
                category = category,
                subcategory = subcategory,
                inputState = InputState.DEFAULT,
                language = language,
                isRepeat = isRepeat,
                period = period,
                limitAmount = limitAmount,
                limitAmountInput = limitAmount.toString(),
                budgetInitialSnapshot = BudgetInitialSnapshot(
                    categoryId = category.id,
                    subcategoryId = subcategory.id,
                    isRepeat = isRepeat,
                    period = period,
                    limitAmount = limitAmount
                )
            )
        }
    }
}

data class BudgetInitialSnapshot(
    val categoryId: Int,
    val subcategoryId: Int,
    val isRepeat: Boolean,
    val period: PeriodType,
    val limitAmount: Double
)
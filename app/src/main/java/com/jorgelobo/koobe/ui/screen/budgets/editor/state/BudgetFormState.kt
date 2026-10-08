package com.jorgelobo.koobe.ui.screen.budgets.editor.state

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory

data class BudgetFormState(
    val category: FieldUpdate<Category> = FieldUpdate.Unchanged,
    val subcategory: FieldUpdate<Subcategory> = FieldUpdate.Unchanged,
    val limitInput: FieldUpdate<String> = FieldUpdate.Unchanged,
    val period: FieldUpdate<PeriodType> = FieldUpdate.Unchanged,
    val isRepeat: FieldUpdate<Boolean> = FieldUpdate.Unchanged,
    val amountKeypadTouched: Boolean = false,
) {
    val hasChanges: Boolean
        get() = category is FieldUpdate.Updated ||
                subcategory is FieldUpdate.Updated ||
                limitInput is FieldUpdate.Updated ||
                period is FieldUpdate.Updated ||
                isRepeat is FieldUpdate.Updated
}
package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

sealed interface BudgetEditorEvent {

    data object ExitToOrigin : BudgetEditorEvent

    data class NavigateTo(val route: String) : BudgetEditorEvent

    data class ShowSnackBar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconPack? = null
    ) : BudgetEditorEvent
}
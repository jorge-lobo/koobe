package com.jorgelobo.koobe.ui.screen.subcategories

import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction

sealed interface SubcategoryEditorIntent {

    // Form field updates — handled synchronously by the Reducer.
    sealed interface State : SubcategoryEditorIntent {
        data class NameChanged(val name: String) : State
        data class IconSelected(val icon: IconPack) : State
        data class CategoryChanged(val categoryId: Int) : State
    }

    // User actions — may trigger coroutines, navigation, or dialog side effects.
    sealed interface Action : SubcategoryEditorIntent {
        object SaveClicked : Action
        object CloseClicked : Action
        object ShowInfoDialog : Action
        object HideInfoDialog : Action
        object RequestDeleteSubcategory : Action

        data class DiscardDialogAction(val action: ConfirmationDialogAction) : Action
        data class DeleteDialogAction(val action: ConfirmationDialogAction) : Action
        data class IconSelectorDialogAction(val action: SelectorDialogAction<IconPack>) : Action
    }
}
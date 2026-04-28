package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction

sealed interface CategoryEditorIntent {

    sealed interface State : CategoryEditorIntent {
        data class NameChanged(val name: String) : State
        data class IconSelected(val icon: IconPack) : State
        data class ColorSelected(val color: Color) : State
        data class TypeSelected(val type: TransactionType) : State
        data class SubcategoriesChanged(val subcategories: List<Subcategory>) : State
    }

    sealed interface Action : CategoryEditorIntent {
        object SaveClicked : Action
        object CloseClicked : Action
        object ShowInfoDialog : Action
        object HideInfoDialog : Action

        data class RequestDeleteSubcategory(val subcategoryId: Int) : Action
        data class SubcategoryEditionAction(val subcategoryId: Int?) : Action
        data class DeleteDialogAction(val action: ConfirmationDialogAction) : Action
        data class DiscardDialogAction(val action: ConfirmationDialogAction) : Action
        data class IconSelectorDialogAction(val action: SelectorDialogAction<IconPack>) : Action
        data class ColorSelectorDialogAction(val action: SelectorDialogAction<Color>) : Action
    }
}
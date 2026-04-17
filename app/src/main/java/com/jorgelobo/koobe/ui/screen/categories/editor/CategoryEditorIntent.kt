package com.jorgelobo.koobe.ui.screen.categories.editor

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

sealed interface CategoryEditorIntent {

    sealed interface State : CategoryEditorIntent {
        data class NameChanged(val name: String) : State
        data class IconSelected(val icon: IconPack) : State
        data class ColorSelected(val color: String) : State
        data class TypeSelected(val type: TransactionType) : State

        object ShowDiscardDialog : State
        object HideDiscardDialog : State
        object ShowDeleteDialog : State
        object HideDeleteDialog : State
        object ShowInfoDialog : State
        object HideInfoDialog : State
        object ShowIconSelectorDialog : State
        object HideIconSelectorDialog : State
        object ShowColorSelectorDialog : State
        object HideColorSelectorDialog : State
    }

    sealed interface Action : CategoryEditorIntent {
        object SaveClicked : Action
        object DeleteClicked : Action
        object BackClicked : Action
    }
}
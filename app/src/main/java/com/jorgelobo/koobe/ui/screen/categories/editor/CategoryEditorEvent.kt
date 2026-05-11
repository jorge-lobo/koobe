package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

sealed class CategoryEditorEvent {
    object NavigateBack : CategoryEditorEvent()
    data class NavigateTo(val route: String) : CategoryEditorEvent()
    data class ShowSnackbar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconPack? = null
    ) : CategoryEditorEvent()
}
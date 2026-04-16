package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

/**
 * One-off UI event for the Category Editor screen.
 *
 */
sealed class CategoryEditorEvent {
    object NavigateBack : CategoryEditorEvent()
    data class NavigateTo(val route: String) : CategoryEditorEvent()

    /**
     * Show snackbar with the given message and optional action.
     */
    data class ShowSnackbar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconPack? = null
    ) : CategoryEditorEvent()
}
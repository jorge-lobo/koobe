package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

sealed interface ShortcutEditorEvent {
    data object NavigateBack : ShortcutEditorEvent
    data class NavigateTo(val route: String) : ShortcutEditorEvent
    data class ShowSnackbar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconPack? = null
    ) : ShortcutEditorEvent
}
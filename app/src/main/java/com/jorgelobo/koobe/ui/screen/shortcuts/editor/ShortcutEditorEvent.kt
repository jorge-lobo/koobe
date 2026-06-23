package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

/**
 * Represents one-time UI events (side effects) emitted by the Shortcut Editor
 */
sealed interface ShortcutEditorEvent {
    data object NavigateBack : ShortcutEditorEvent
    data class NavigateTo(val route: String) : ShortcutEditorEvent

    /**
     * Event to trigger the display of a snackbar in the shortcut editor screen.
     *
     * @property messageRes The string resource ID for the message to be displayed.
     * @property actionLabelRes The optional string resource ID for the action button label.
     * @property icon The optional [IconPack] to be displayed alongside the message.
     */
    data class ShowSnackbar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconPack? = null
    ) : ShortcutEditorEvent
}
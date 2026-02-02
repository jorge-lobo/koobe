package com.jorgelobo.koobe.ui.screen.transactions

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral

/**
 * Represents one-off events emitted by [TransactionEditorViewModel] to the UI.
 *
 * These events are typically used for navigation, snackBars, or other transient UI effects that
 * should not be stored in the state.
 */
sealed interface TransactionEditorEvent {

    /** Signals that the editor should exit and return to the originating screen. */
    data object ExitToOrigin : TransactionEditorEvent

    /**
     * Requests showing a snackBar in the UI.
     *
     * @param messageRes The string resource ID for the main message.
     * @param actionLabelRes Optional string resource ID for the action button.
     * @param icon Optional icon to display in the snackBar.
     */
    data class ShowSnackBar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconGeneral? = null
    ) : TransactionEditorEvent
}
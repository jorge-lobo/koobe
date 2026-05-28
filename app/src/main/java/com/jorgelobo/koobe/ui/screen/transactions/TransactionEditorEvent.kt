package com.jorgelobo.koobe.ui.screen.transactions

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

/**
 * One-off events emitted by the Transaction Editor ViewModel to be handled by the UI layer.
 *
 * These events represent actions that should not be part of the persistent UI state,
 * such as navigation or transient UI feedback.
 */
sealed interface TransactionEditorEvent {

    /** Closes the editor and navigates back to the origin screen. */
    data object ExitToOrigin : TransactionEditorEvent

    /**
     * Requests navigation to a specific route.
     *
     * @property route Destination navigation route.
     */
    data class NavigateTo(val route: String) : TransactionEditorEvent

    /**
     * Requests showing a snackBar in the UI.
     *
     * @property messageRes String resource ID for the main message.
     * @property actionLabelRes Optional string resource ID for the action button.
     * @property icon Optional icon to display in the snackBar.
     */
    data class ShowSnackBar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconPack? = null
    ) : TransactionEditorEvent
}
package com.jorgelobo.koobe.ui.screen.subcategories.state

import com.jorgelobo.koobe.ui.components.composed.dialogs.ConfirmationDialog
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

/**
 * Internal UI state for subcategory screen dialogs and transient flags.
 */
data class SubcategoryUiStateInternal(
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val infoDialog: InfoDialogState = InfoDialogState(),
    val iconSelectorDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false
)
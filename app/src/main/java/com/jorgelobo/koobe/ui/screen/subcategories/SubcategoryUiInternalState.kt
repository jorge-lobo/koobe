package com.jorgelobo.koobe.ui.screen.subcategories

import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

/**
 * Internal UI state for subcategory screen dialogs and transient flags.
 */
data class SubcategoryUiStateInternal(
    val discardDialog: ConfirmationDialogState? = null,
    val deleteDialog: ConfirmationDialogState? = null,
    val infoDialog: InfoDialogState? = null,
    val iconSelectorDialog: SelectorDialogState<IconPack>? = null,
    val isDeleting: Boolean = false
)
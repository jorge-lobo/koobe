package com.jorgelobo.koobe.ui.screen.categories.editor.state

import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

data class CategoryUiStateInternal(
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val infoDialog: InfoDialogState = InfoDialogState(),
    val iconSelectorDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val colorSelectorDialog: SelectorDialogState<String> = SelectorDialogState(),
    val isDeleting: Boolean = false
)
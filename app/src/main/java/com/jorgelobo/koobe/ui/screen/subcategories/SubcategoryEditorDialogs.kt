package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.dialogs.AvatarConfigurationDialogConfig
import com.jorgelobo.koobe.ui.components.composed.dialogs.DeleteDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.IconSelectorDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.InfoDialog
import com.jorgelobo.koobe.ui.components.model.enums.AvatarConfigurationType
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType
import com.jorgelobo.koobe.ui.components.model.enums.InfoType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction

@Composable
fun SubcategoryEditorDialogs(
    state: SubcategoryEditorUiState,
    onDiscardDialogAction: (ConfirmationDialogAction) -> Unit,
    onDeleteDialogAction: (ConfirmationDialogAction) -> Unit,
    onInfoDialogClick: () -> Unit,
    onIconSelectorAction: (SelectorDialogAction<IconPack>) -> Unit
) {
    if (state.discardDialog.visible) {
        DiscardDialog(
            onConfirm = { onDiscardDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDiscardDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.deleteDialog.visible) {
        DeleteDialog(
            type = DeleteType.SUBCATEGORY,
            transactionType = state.category.type,
            onConfirm = { onDeleteDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDeleteDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.infoDialog.visible) {
        InfoDialog(
            type = InfoType.SUBCATEGORY,
            onClick = onInfoDialogClick
        )
    }

    if (state.iconDialog.visible) {
        IconSelectorDialog(
            state = state.iconDialog,
            onAction = onIconSelectorAction,
            config = AvatarConfigurationDialogConfig(
                type = AvatarConfigurationType.ICON,
                onApply = { onIconSelectorAction(SelectorDialogAction.Apply) },
                onCancel = { onIconSelectorAction(SelectorDialogAction.Cancel) }
            )
        )
    }
}
package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.ui.components.composed.dialogs.AvatarConfigurationDialogConfig
import com.jorgelobo.koobe.ui.components.composed.dialogs.ColorSelectorDialog
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
fun CategoryEditorDialogs(
    state: CategoryEditorUiState,
    onDiscardDialogAction: (ConfirmationDialogAction) -> Unit,
    onDeleteDialogAction: (ConfirmationDialogAction) -> Unit,
    onInfoDialogClick: () -> Unit,
    onIconSelectorAction: (SelectorDialogAction<IconPack>) -> Unit,
    onColorSelectorAction: (SelectorDialogAction<Color>) -> Unit
) {
    if (state.discardDialog.visible) {
        DiscardDialog(
            onConfirm = { onDiscardDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDiscardDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.deleteDialog.visible) {
        DeleteDialog(
            type = DeleteType.CATEGORY,
            transactionType = state.category.type,
            onConfirm = { onDeleteDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDeleteDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.iconDialog.visible) {
        InfoDialog(
            type = InfoType.CATEGORY,
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

    if (state.colorDialog.visible) {
        ColorSelectorDialog(
            state = state.colorDialog,
            onAction = onColorSelectorAction,
            config = AvatarConfigurationDialogConfig(
                type = AvatarConfigurationType.COLOR,
                onApply = { onColorSelectorAction(SelectorDialogAction.Apply) },
                onCancel = { onColorSelectorAction(SelectorDialogAction.Cancel) }
            )
        )
    }
}
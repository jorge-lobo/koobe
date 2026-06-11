package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.composed.dialogs.AvatarConfigurationDialogConfig
import com.jorgelobo.koobe.ui.components.composed.dialogs.DeleteDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.IconSelectorDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialogConfig
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheet
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheetConfig
import com.jorgelobo.koobe.ui.components.model.enums.AvatarConfigurationType
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType
import com.jorgelobo.koobe.ui.components.model.enums.OptionSelectorType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutEditorDialogs(
    state: ShortcutEditorUiState,
    sheetState: SheetState,
    onDiscardDialogAction: (ConfirmationDialogAction) -> Unit,
    onDeleteDialogAction: (ConfirmationDialogAction) -> Unit,
    onIconSelectorDialogAction: (SelectorDialogAction<IconPack>) -> Unit,
    onCurrencyDialogAction: (SelectorDialogAction<CurrencyType>) -> Unit,
    onPeriodSelectorAction: (SelectorSheetAction<PeriodType>) -> Unit,
    onPaymentMethodSelectorAction: (SelectorSheetAction<PaymentMethodType>) -> Unit
) {
    if (state.discardDialog.visible) {
        DiscardDialog(
            onConfirm = { onDiscardDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDiscardDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.deleteDialog.visible) {
        DeleteDialog(
            type = DeleteType.SHORTCUT,
            onConfirm = { onDeleteDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDeleteDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.iconSelectorDialog.visible) {
        IconSelectorDialog(
            state = state.iconSelectorDialog,
            onAction = onIconSelectorDialogAction,
            config = AvatarConfigurationDialogConfig(
                type = AvatarConfigurationType.ICON,
                onApply = { onIconSelectorDialogAction(SelectorDialogAction.Apply) },
                onCancel = { onIconSelectorDialogAction(SelectorDialogAction.Cancel) }
            )
        )
    }

    if (state.currencySelectorDialog.visible) {
        OptionSelectorDialog(
            config = OptionSelectorDialogConfig(
                type = OptionSelectorType.CURRENCY,
                title = stringResource(R.string.dialog_headline_currency_selector),
                selectedCurrency = state.currencySelectorDialog.selected
                    ?: state.currencySelectorDialog.initial
                    ?: CurrencyType.EUR,
                onConfirm = { onCurrencyDialogAction(SelectorDialogAction.Apply) },
                onCancel = { onCurrencyDialogAction(SelectorDialogAction.Cancel) },
                onCurrencySelected = { onCurrencyDialogAction(SelectorDialogAction.Select(it)) }
            )
        )
    }

    if (state.paymentMethodSelectorSheet.visible) {
        ModalBottomSheet(
            onDismissRequest = { onPaymentMethodSelectorAction(SelectorSheetAction.Dismiss) }
        ) {
            ListSelectorBottomSheet(
                sheetState = sheetState,
                config = ListSelectorBottomSheetConfig.Payment(
                    selected = state.paymentMethodSelectorSheet.selected,
                    onItemSelected = { onPaymentMethodSelectorAction(SelectorSheetAction.Select(it)) }
                ),
                onDismiss = { onPaymentMethodSelectorAction(SelectorSheetAction.Dismiss) }
            )
        }
    }

    if (state.periodSelectorSheet.visible) {
        ModalBottomSheet(
            onDismissRequest = { onPeriodSelectorAction(SelectorSheetAction.Dismiss) }
        ) {
            ListSelectorBottomSheet(
                sheetState = sheetState,
                config = ListSelectorBottomSheetConfig.Period(
                    selected = state.periodSelectorSheet.selected,
                    onItemSelected = { onPeriodSelectorAction(SelectorSheetAction.Select(it)) }
                ),
                onDismiss = { onPeriodSelectorAction(SelectorSheetAction.Dismiss) }
            )
        }
    }
}
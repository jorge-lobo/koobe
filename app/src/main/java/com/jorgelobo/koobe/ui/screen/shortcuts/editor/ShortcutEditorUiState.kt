package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

data class ShortcutEditorUiState(
    val config: ShortcutEditorConfig? = null,
    val originalShortcut: Shortcut? = null,
    val category: Category,
    val inputState: InputState,
    val paymentMethodType: PaymentMethodType = PaymentMethodType.CASH,
    val currencyType: CurrencyType = CurrencyType.EUR,
    val name: String = "",
    val icon: IconPack = IconPack.PLACEHOLDER,
    val amountInput: String = "0",
    val amount: Double = 0.0,
    val repeat: Boolean = false,
    val repeatFrequency: PeriodType? = PeriodType.DAILY,
    val shortcutInitialSnapshot: ShortcutInitialSnapshot,
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val iconSelectDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val currencyDialog: SelectorDialogState<CurrencyType> = SelectorDialogState(),
    val paymentMethodSelector: SelectorSheetState<PaymentMethodType>,
    val periodSelector: SelectorSheetState<PeriodType>,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false
) {
    val isValid: Boolean
        get() = name.isNotBlank() && amount > 0 && icon != IconPack.PLACEHOLDER

    val isSaveEnabled: Boolean
        get() {
            val config = config ?: return false

            if (!isValid) return false

            return if (config.isEditMode) {
                val initial = shortcutInitialSnapshot

                category.id != initial.category.id ||
                        name != initial.name ||
                        icon != initial.icon ||
                        amount != initial.amount ||
                        repeat != initial.repeat ||
                        repeatFrequency != initial.repeatFrequency ||
                        paymentMethodType != initial.paymentMethodType ||
                        currencyType != initial.currencyType
            } else {
                true
            }
        }

    fun headlineRes(isEditMode: Boolean): Int =
        if (isEditMode) {
            R.string.headline_shortcut_editor
        } else {
            R.string.headline_shortcut_creator
        }

    companion object {
        fun initialEmpty(): ShortcutEditorUiState {
            val emptyCategory = Category.empty()

            return ShortcutEditorUiState(
                category = emptyCategory,
                originalShortcut = null,
                inputState = InputState.DEFAULT,
                isLoading = true,
                shortcutInitialSnapshot = ShortcutInitialSnapshot(
                    category = emptyCategory,
                    name = "",
                    icon = IconPack.PLACEHOLDER,
                    amount = 0.0,
                    repeat = false,
                    repeatFrequency = PeriodType.DAILY,
                    paymentMethodType = PaymentMethodType.CASH,
                    currencyType = CurrencyType.EUR
                ),
                paymentMethodSelector = SelectorSheetState(
                    visible = false,
                    selected = PaymentMethodType.CASH
                ),
                periodSelector = SelectorSheetState(
                    visible = false,
                    selected = PeriodType.DAILY
                )
            )
        }

        fun initial(
            config: ShortcutEditorConfig,
            category: Category
        ): ShortcutEditorUiState {
            return ShortcutEditorUiState(
                config = config,
                originalShortcut = null,
                category = category,
                inputState = InputState.DEFAULT,
                shortcutInitialSnapshot = ShortcutInitialSnapshot(
                    category = category,
                    name = "",
                    icon = IconPack.PLACEHOLDER,
                    amount = 0.0,
                    repeat = false,
                    repeatFrequency = PeriodType.DAILY,
                    paymentMethodType = PaymentMethodType.CASH,
                    currencyType = CurrencyType.EUR
                ),
                paymentMethodSelector = SelectorSheetState(
                    visible = false,
                    selected = PaymentMethodType.CASH
                ),
                periodSelector = SelectorSheetState(
                    visible = false,
                    selected = PeriodType.DAILY
                )
            )
        }

        fun initialFromShortcut(
            config: ShortcutEditorConfig,
            shortcut: Shortcut,
            category: Category
        ): ShortcutEditorUiState {
            return ShortcutEditorUiState(
                config = config,
                originalShortcut = shortcut,
                category = category,
                inputState = InputState.DEFAULT,
                name = shortcut.name,
                icon = shortcut.icon,
                amountInput = shortcut.amount.toString(),
                amount = shortcut.amount,
                repeat = shortcut.repeat,
                repeatFrequency = shortcut.period,
                paymentMethodType = shortcut.paymentMethod,
                currencyType = shortcut.currency,
                shortcutInitialSnapshot = ShortcutInitialSnapshot(
                    category = category,
                    name = shortcut.name,
                    icon = shortcut.icon,
                    amount = shortcut.amount,
                    repeat = shortcut.repeat,
                    repeatFrequency = shortcut.period,
                    paymentMethodType = shortcut.paymentMethod,
                    currencyType = shortcut.currency
                ),
                paymentMethodSelector = SelectorSheetState(
                    visible = false,
                    selected = shortcut.paymentMethod
                ),
                periodSelector = SelectorSheetState(
                    visible = false,
                    selected = shortcut.period ?: PeriodType.DAILY
                )
            )
        }
    }
}

data class ShortcutInitialSnapshot(
    val category: Category,
    val name: String,
    val icon: IconPack,
    val amount: Double,
    val repeat: Boolean,
    val repeatFrequency: PeriodType?,
    val paymentMethodType: PaymentMethodType,
    val currencyType: CurrencyType
)
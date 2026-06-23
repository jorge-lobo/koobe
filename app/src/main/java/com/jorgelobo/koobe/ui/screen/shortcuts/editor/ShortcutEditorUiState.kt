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

/**
 * Represents the UI state for the Shortcut Editor screen, supporting both the creation of new
 * shortcuts and the modification of existing ones.
 *
 * This state manages input fields, validation logic, and the visibility of various selection
 * dialogs and bottom sheets required for configuring a transaction shortcut.
 */
data class ShortcutEditorUiState(
    val config: ShortcutEditorConfig? = null,
    val originalShortcut: Shortcut? = null,
    val category: Category,
    val nameInputState: InputState,
    val paymentMethodType: PaymentMethodType = PaymentMethodType.CASH,
    val currencyType: CurrencyType = CurrencyType.EUR,
    val name: String = "",
    val icon: IconPack = IconPack.PLACEHOLDER,
    val amountInput: String = "0",
    val amount: Double = 0.0,
    val repeat: Boolean = false,
    val repeatFrequency: PeriodType? = null,
    val shortcutInitialSnapshot: ShortcutInitialSnapshot,
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val iconSelectorDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val currencySelectorDialog: SelectorDialogState<CurrencyType> = SelectorDialogState(),
    val paymentMethodSelectorSheet: SelectorSheetState<PaymentMethodType>,
    val periodSelectorSheet: SelectorSheetState<PeriodType>,
    val hasNameError: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false
) {

    // Indicates whether the shortcut data is valid for saving.
    val isValid: Boolean
        get() = name.isNotBlank() &&
                amount > 0 &&
                icon != IconPack.PLACEHOLDER &&
                (!repeat || repeatFrequency != null)

    /**
     * Indicates whether the current state allows saving the shortcut.
     *
     * Returns true if the inputs are valid and, in the case of editing an existing shortcut, if any
     * changes have been made compared to the initial snapshot.
     */
    val isSaveEnabled: Boolean
        get() {
            val config = config ?: return false

            if (!isValid) return false

            return when (config) {
                is ShortcutEditorConfig.Create -> true

                is ShortcutEditorConfig.Edit -> {
                    val initial = shortcutInitialSnapshot

                    category.id != initial.categoryId ||
                            name != initial.name ||
                            icon != initial.icon ||
                            amount != initial.amount ||
                            repeat != initial.repeat ||
                            repeatFrequency != initial.repeatFrequency ||
                            paymentMethodType != initial.paymentMethodType ||
                            currencyType != initial.currencyType
                }
            }
        }

    // Returns the screen title string resource based on the current mode.
    val headlineRes: Int
        get() = when (config) {
            is ShortcutEditorConfig.Create -> R.string.headline_shortcut_creator
            else -> R.string.headline_shortcut_editor
        }

    companion object {
        fun initialEmpty(): ShortcutEditorUiState {
            val emptyCategory = Category.empty()

            return ShortcutEditorUiState(
                category = emptyCategory,
                nameInputState = InputState.DEFAULT,
                shortcutInitialSnapshot = ShortcutInitialSnapshot(
                    categoryId = emptyCategory.id,
                    name = "",
                    icon = IconPack.PLACEHOLDER,
                    amount = 0.0,
                    repeat = false,
                    repeatFrequency = null,
                    paymentMethodType = PaymentMethodType.CASH,
                    currencyType = CurrencyType.EUR
                ),
                paymentMethodSelectorSheet = SelectorSheetState(
                    visible = false,
                    selected = PaymentMethodType.CASH
                ),
                periodSelectorSheet = SelectorSheetState(
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
                category = category,
                nameInputState = InputState.DEFAULT,
                shortcutInitialSnapshot = ShortcutInitialSnapshot(
                    categoryId = category.id,
                    name = "",
                    icon = IconPack.PLACEHOLDER,
                    amount = 0.0,
                    repeat = false,
                    repeatFrequency = null,
                    paymentMethodType = PaymentMethodType.CASH,
                    currencyType = CurrencyType.EUR
                ),
                paymentMethodSelectorSheet = SelectorSheetState(
                    visible = false,
                    selected = PaymentMethodType.CASH
                ),
                periodSelectorSheet = SelectorSheetState(
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
                nameInputState = InputState.DEFAULT,
                name = shortcut.name,
                icon = shortcut.icon,
                amountInput = shortcut.amount.toString(),
                amount = shortcut.amount,
                repeat = shortcut.repeat,
                repeatFrequency = shortcut.period,
                paymentMethodType = shortcut.paymentMethod,
                currencyType = shortcut.currency,
                shortcutInitialSnapshot = ShortcutInitialSnapshot(
                    categoryId = category.id,
                    name = shortcut.name,
                    icon = shortcut.icon,
                    amount = shortcut.amount,
                    repeat = shortcut.repeat,
                    repeatFrequency = shortcut.period,
                    paymentMethodType = shortcut.paymentMethod,
                    currencyType = shortcut.currency
                ),
                paymentMethodSelectorSheet = SelectorSheetState(
                    visible = false,
                    selected = shortcut.paymentMethod
                ),
                periodSelectorSheet = SelectorSheetState(
                    visible = false,
                    selected = shortcut.period ?: PeriodType.DAILY
                )
            )
        }
    }
}

/**
 * A data snapshot representing the initial state of a [Shortcut] when the editor is opened.
 *
 * This class is used to track changes by comparing the current input values in [ShortcutEditorUiState]
 * against these initial values, enabling the system to determine if the "Save" action should be
 * enabled based on whether any modifications have been made.
 *
 * @property categoryId The unique identifier of the category initially assigned.
 * @property name The initial name of the shortcut.
 * @property icon The initial icon selected for the shortcut.
 * @property amount The initial monetary value.
 * @property repeat The initial boolean state indicating if the transaction is recurring.
 * @property repeatFrequency The initial frequency (e.g., Daily, Monthly) if the shortcut is recurring.
 * @property paymentMethodType The initial payment method (e.g., Cash, Credit Card).
 * @property currencyType The initial currency used for the amount.
 */
data class ShortcutInitialSnapshot(
    val categoryId: Int,
    val name: String,
    val icon: IconPack,
    val amount: Double,
    val repeat: Boolean,
    val repeatFrequency: PeriodType?,
    val paymentMethodType: PaymentMethodType,
    val currencyType: CurrencyType
)
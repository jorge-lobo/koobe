package com.jorgelobo.koobe.ui.screen.shortcuts.editor.state

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class ShortcutFormState(
    val name: FieldUpdate<String> = FieldUpdate.Unchanged,
    val icon: FieldUpdate<IconPack> = FieldUpdate.Unchanged,
    val amountInput: FieldUpdate<String> = FieldUpdate.Unchanged,
    val repeat: FieldUpdate<Boolean> = FieldUpdate.Unchanged,
    val repeatFrequency: FieldUpdate<PeriodType?> = FieldUpdate.Unchanged,
    val paymentMethod: FieldUpdate<PaymentMethodType> = FieldUpdate.Unchanged,
    val currency: FieldUpdate<CurrencyType> = FieldUpdate.Unchanged,
    val amountKeypadTouched: Boolean = false
) {
    val hasChanges: Boolean
        get() = name is FieldUpdate.Updated ||
                icon is FieldUpdate.Updated ||
                amountInput is FieldUpdate.Updated ||
                repeat is FieldUpdate.Updated ||
                repeatFrequency is FieldUpdate.Updated ||
                paymentMethod is FieldUpdate.Updated ||
                currency is FieldUpdate.Updated
}
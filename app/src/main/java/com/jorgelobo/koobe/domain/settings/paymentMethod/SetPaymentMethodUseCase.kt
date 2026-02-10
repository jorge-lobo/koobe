package com.jorgelobo.koobe.domain.settings.paymentMethod

import com.jorgelobo.koobe.data.settings.SettingsPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import javax.inject.Inject

class SetPaymentMethodUseCase @Inject constructor(
    private val preferences: SettingsPreferences
) {
    suspend operator fun invoke(paymentMethod: PaymentMethodType) {
        preferences.setPaymentMethod(paymentMethod)
    }
}
package com.jorgelobo.koobe.domain.settings.paymentMethod

import com.jorgelobo.koobe.data.settings.SettingsPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentMethodUseCase @Inject constructor(
    private val preferences: SettingsPreferences
) {
    operator fun invoke(): Flow<PaymentMethodType> =
        preferences.paymentMethodFlow
}
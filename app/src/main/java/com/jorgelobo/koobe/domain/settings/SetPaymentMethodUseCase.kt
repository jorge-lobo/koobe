package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import javax.inject.Inject

class SetPaymentMethodUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(paymentMethod: PaymentMethodType) {
        repository.setPaymentMethod(paymentMethod)
    }
}
package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import javax.inject.Inject

/**
 * Use case responsible for updating the user's preferred payment method.
 *
 * @property repository The [SettingsRepository] used to persist the payment method selection.
 */
class SetPaymentMethodUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(paymentMethod: PaymentMethodType) {
        repository.setPaymentMethod(paymentMethod)
    }
}
package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import javax.inject.Inject

class SetCurrencyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(currency: CurrencyType) {
        repository.setCurrency(currency)
    }
}
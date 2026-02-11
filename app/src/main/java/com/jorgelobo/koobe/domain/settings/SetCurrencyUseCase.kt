package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import javax.inject.Inject

/**
 * Use case responsible for updating the user's preferred currency setting.
 *
 * @property repository The repository used to persist the currency selection.
 */
class SetCurrencyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(currency: CurrencyType) {
        repository.setCurrency(currency)
    }
}
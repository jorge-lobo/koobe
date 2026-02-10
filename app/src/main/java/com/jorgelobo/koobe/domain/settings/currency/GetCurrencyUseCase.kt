package com.jorgelobo.koobe.domain.settings.currency

import com.jorgelobo.koobe.data.settings.SettingsPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrencyUseCase @Inject constructor(
    private val preferences: SettingsPreferences
) {
    operator fun invoke(): Flow<CurrencyType> = preferences.currencyFlow
}
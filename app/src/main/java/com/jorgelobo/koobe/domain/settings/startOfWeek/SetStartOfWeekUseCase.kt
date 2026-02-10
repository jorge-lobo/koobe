package com.jorgelobo.koobe.domain.settings.startOfWeek

import com.jorgelobo.koobe.data.settings.SettingsPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import javax.inject.Inject

class SetStartOfWeekUseCase @Inject constructor(
    private val preferences: SettingsPreferences
) {
    suspend operator fun invoke(startOfWeek: StartOfWeek) {
        preferences.setStartOfWeek(startOfWeek)
    }
}
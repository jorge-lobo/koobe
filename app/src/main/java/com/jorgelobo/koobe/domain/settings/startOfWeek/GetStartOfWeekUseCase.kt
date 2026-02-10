package com.jorgelobo.koobe.domain.settings.startOfWeek

import com.jorgelobo.koobe.data.settings.SettingsPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStartOfWeekUseCase @Inject constructor(
    private val preferences: SettingsPreferences
) {
    operator fun invoke(): Flow<StartOfWeek> =
        preferences.startOfWeekFlow
}
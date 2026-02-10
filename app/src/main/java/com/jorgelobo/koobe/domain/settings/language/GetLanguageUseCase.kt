package com.jorgelobo.koobe.domain.settings.language

import com.jorgelobo.koobe.data.settings.SettingsPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val preferences: SettingsPreferences
) {
    operator fun invoke(): Flow<AppLanguage> =
        preferences.languageFlow
}
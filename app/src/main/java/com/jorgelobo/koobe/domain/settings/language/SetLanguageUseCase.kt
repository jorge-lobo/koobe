package com.jorgelobo.koobe.domain.settings.language

import com.jorgelobo.koobe.data.settings.SettingsPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val preferences: SettingsPreferences
) {
    suspend operator fun invoke(language: AppLanguage) {
        preferences.setLanguage(language)
    }
}
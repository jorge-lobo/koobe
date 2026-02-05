package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.data.preferences.AppPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import javax.inject.Inject

class SetThemeOptionUseCase @Inject constructor(
    private val preferences: AppPreferences
) {
    suspend operator fun invoke(option: ThemeOption) {
        preferences.setThemeOption(option)
    }
}
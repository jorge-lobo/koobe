package com.jorgelobo.koobe.domain.settings.theme

import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.settings.theme.SettingsRepository
import javax.inject.Inject

class SaveThemeOptionUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(option: ThemeOption) {
        repository.saveThemeOption(option)
    }
}
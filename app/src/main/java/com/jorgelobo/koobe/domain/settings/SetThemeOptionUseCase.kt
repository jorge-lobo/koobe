package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import javax.inject.Inject

class SetThemeOptionUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(option: ThemeOption) {
        repository.setTheme(option)
    }
}
package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.data.preferences.AppPreferences
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeOptionUseCase @Inject constructor(
    private val preferences: AppPreferences
) {
    operator fun invoke(): Flow<ThemeOption> =
        preferences.themeOption()
}
package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import javax.inject.Inject

/**
 * Use case responsible for updating the application's language setting.
 *
 * @property repository The [SettingsRepository] used to persist the selected language.
 */
class SetLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(language: AppLanguage) {
        repository.setLanguage(language)
    }
}
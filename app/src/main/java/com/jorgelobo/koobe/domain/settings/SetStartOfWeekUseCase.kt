package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import javax.inject.Inject

/**
 * Use case responsible for updating the preferred starting day of the week in the application settings.
 *
 * @property repository The [SettingsRepository] used to persist the setting.
 */
class SetStartOfWeekUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(startOfWeek: StartOfWeek) {
        repository.setStartOfWeek(startOfWeek)
    }
}
package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.settings.UserSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve the current user settings as a [Flow] of [UserSettings].
 *
 * This class interacts with the [SettingsRepository] to provide a continuous stream
 * of configuration preferences.
 *
 * @property repository The repository responsible for providing the user settings data.
 */
class GetUserSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<UserSettings> =
        repository.userSettings()
}
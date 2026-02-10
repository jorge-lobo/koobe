package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.settings.UserSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<UserSettings> =
        repository.userSettings()
}
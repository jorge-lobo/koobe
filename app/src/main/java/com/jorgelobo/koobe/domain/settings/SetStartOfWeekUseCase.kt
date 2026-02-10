package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import javax.inject.Inject

class SetStartOfWeekUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(startOfWeek: StartOfWeek) {
        repository.setStartOfWeek(startOfWeek)
    }
}
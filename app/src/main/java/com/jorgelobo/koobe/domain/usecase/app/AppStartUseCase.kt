package com.jorgelobo.koobe.domain.usecase.app

import com.jorgelobo.koobe.domain.usecase.shortcut.ExecuteScheduledShortcutsUseCase
import javax.inject.Inject

class AppStartUseCase @Inject constructor(
    private val ensureDefaultData: EnsureDefaultDataUseCase,
    private val executeScheduledShortcuts: ExecuteScheduledShortcutsUseCase
) {

    suspend operator fun invoke(): Int {
        ensureDefaultData()
        return executeScheduledShortcuts()
    }
}
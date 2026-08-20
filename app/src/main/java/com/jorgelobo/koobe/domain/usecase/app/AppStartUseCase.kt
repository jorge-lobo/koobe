package com.jorgelobo.koobe.domain.usecase.app

import com.jorgelobo.koobe.domain.usecase.shortcut.ExecuteScheduledShortcutsUseCase
import javax.inject.Inject

/**
 * Executes all currently due scheduled shortcuts.
 *
 * @return The number of shortcuts that were executed.
 */
class AppStartUseCase @Inject constructor(
    private val ensureDefaultData: EnsureDefaultDataUseCase,
    private val executeScheduledShortcuts: ExecuteScheduledShortcutsUseCase
) {

    /**
     * Performs the application's startup tasks.
     *
     * Ensures that the default application data exists and executes any scheduled shortcuts
     * that are due.
     *
     * @return The number of scheduled shortcuts executed during startup.
     */
    suspend operator fun invoke(): Int {
        ensureDefaultData()
        return executeScheduledShortcuts()
    }
}
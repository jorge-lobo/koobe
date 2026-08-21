package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.shortcut.Shortcut
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Retrieves all recurring shortcuts that are due for execution.
 *
 * A shortcut is considered due when it satisfies the conditions defined by
 * [ShouldExecuteShortcutUseCase].
 */
class GetDueShortcutsUseCase @Inject constructor(
    private val repository: ShortcutRepository,
    private val shouldExecuteShortcut: ShouldExecuteShortcutUseCase
) {

    /**
     * Returns all recurring shortcuts that are currently due for execution.
     *
     * A shortcut is considered due according to its recurrence configuration and last execution date.
     */
    suspend operator fun invoke(): List<Shortcut> =
        repository
            .getAllShortcuts()
            .first()
            .filter(shouldExecuteShortcut::invoke)
}
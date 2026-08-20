package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import java.util.Date
import javax.inject.Inject

/**
 * Updates the last execution date of a shortcut.
 *
 * Persists the provided execution date for the given shortcut in the [ShortcutRepository].
 */
class UpdateShortcutLastExecutionUseCase @Inject constructor(
    private val repository: ShortcutRepository
) {

    /**
     * Updates the last execution date of the specified shortcut.
     *
     * @param shortcutId The unique identifier of the shortcut to update.
     * @param date The date of the most recent scheduled execution.
     */
    suspend operator fun invoke(
        shortcutId: Int,
        date: Date
    ) {
        repository.updateLastExecutionDate(
            shortcutId = shortcutId,
            date = date.time
        )
    }
}
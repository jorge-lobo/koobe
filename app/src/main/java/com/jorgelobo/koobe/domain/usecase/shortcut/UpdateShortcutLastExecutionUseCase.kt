package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
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
     * Updates the shortcut with the specified last execution [date].
     */
    suspend operator fun invoke(
        shortcut: Shortcut,
        date: Date
    ) {
        repository.updateShortcut(
            shortcut.copy(
                lastExecutionDate = date
            )
        )
    }
}
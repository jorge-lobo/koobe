package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.usecase.transaction.CreateTransactionFromShortcutUseCase
import javax.inject.Inject

/**
 * Executes all scheduled shortcuts that are currently due.
 *
 * Retrieves all due shortcuts, creates transactions for each missing occurrence, and updates each
 * shortcut's last execution date.
 */
class ExecuteScheduledShortcutsUseCase @Inject constructor(
    private val getDueShortcuts: GetDueShortcutsUseCase,
    private val calculateMissingOccurrences: CalculateMissingOccurrencesUseCase,
    private val createTransaction: CreateTransactionFromShortcutUseCase,
    private val updateLastExecution: UpdateShortcutLastExecutionUseCase
) {

    /**
     * Executes all scheduled shortcuts that are currently due.
     */
    suspend operator fun invoke() {
        val dueShortcuts = getDueShortcuts()

        dueShortcuts.forEach { shortcut ->
            val occurrences = calculateMissingOccurrences(shortcut)

            occurrences.forEach { date ->
                createTransaction(
                    shortcut = shortcut,
                    date = date,
                    incrementUsage = false
                )
            }

            occurrences.lastOrNull()?.let {
                updateLastExecution(
                    shortcut = shortcut,
                    date = it
                )
            }
        }
    }
}
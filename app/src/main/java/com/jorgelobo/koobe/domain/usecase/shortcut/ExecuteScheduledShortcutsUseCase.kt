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
    suspend operator fun invoke(): Int {
        val dueShortcuts = getDueShortcuts()
        var executedShortcuts = 0

        dueShortcuts.forEach { shortcut ->
            val occurrences = calculateMissingOccurrences(shortcut)

            if (occurrences.isNotEmpty()) {
                occurrences.forEach { date ->
                    createTransaction(
                        shortcut = shortcut,
                        date = date,
                        incrementUsage = false
                    )
                }

                updateLastExecution(
                    shortcutId = shortcut.id,
                    date = occurrences.last()
                )

                executedShortcuts++
            }
        }

        return executedShortcuts
    }
}
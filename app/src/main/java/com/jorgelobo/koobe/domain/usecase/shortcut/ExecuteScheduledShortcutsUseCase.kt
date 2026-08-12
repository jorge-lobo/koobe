package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.usecase.transaction.CreateTransactionFromShortcutUseCase
import javax.inject.Inject

/**
 * Executes all scheduled shortcuts that are currently due.
 *
 * For each due shortcut, creates transactions for all missing occurrences and updates its last
 * execution date. Automatic executions do not increment the shortcut usage count.
 *
 * @return The number of shortcuts for which at least one transaction was created.
 */
class ExecuteScheduledShortcutsUseCase @Inject constructor(
    private val getDueShortcuts: GetDueShortcutsUseCase,
    private val calculateMissingOccurrences: CalculateMissingOccurrencesUseCase,
    private val createTransaction: CreateTransactionFromShortcutUseCase,
    private val updateLastExecution: UpdateShortcutLastExecutionUseCase
) {

    /**
     * Executes all currently due scheduled shortcuts.
     *
     * @return The number of shortcuts that were executed.
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
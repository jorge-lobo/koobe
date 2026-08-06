package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.usecase.transaction.CreateTransactionFromShortcutUseCase
import javax.inject.Inject

class ExecuteScheduledShortcutsUseCase @Inject constructor(
    private val getDueShortcuts: GetDueShortcutsUseCase,
    private val calculateMissingOccurrences: CalculateMissingOccurrencesUseCase,
    private val createTransaction: CreateTransactionFromShortcutUseCase,
    private val updateLastExecution: UpdateShortcutLastExecutionUseCase
) {
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
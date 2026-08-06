package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.usecase.shortcut.IncrementShortcutUsageUseCase
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date
import javax.inject.Inject

/**
 * Creates a transaction from a shortcut.
 *
 * Inserts a new transaction using the shortcut's data and optionally increments the shortcut's
 * usage count.
 */
class CreateTransactionFromShortcutUseCase @Inject constructor(
    private val insertTransaction: InsertTransactionUseCase,
    private val incrementShortcutUsage: IncrementShortcutUsageUseCase
) {

    /**
     * Creates a transaction from the given [shortcut].
     */
    suspend operator fun invoke(
        shortcut: Shortcut,
        date: Date = DateUtils.currentDate,
        incrementUsage: Boolean = true
    ) {
        val transaction = Transaction(
            id = 0,
            categoryId = shortcut.categoryId,
            subcategoryId = null,
            amount = shortcut.amount,
            currency = shortcut.currency,
            paymentMethod = shortcut.paymentMethod,
            type = shortcut.transactionType,
            date = date,
            description = shortcut.name,
        )

        insertTransaction(transaction)

        if (incrementUsage) {
            incrementShortcutUsage(shortcut.id)
        }
    }
}
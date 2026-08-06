package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.usecase.shortcut.IncrementShortcutUsageUseCase
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date
import javax.inject.Inject

/**
 * Use case responsible for creating and persisting a new [Transaction] based on a predefined [Shortcut].
 *
 * This use case maps properties from a shortcut to a new transaction, saves it using [InsertTransactionUseCase],
 * and optionally updates the shortcut's usage frequency via [IncrementShortcutUsageUseCase].
 *
 * @property insertTransaction The use case used to persist the newly created transaction.
 * @property incrementShortcutUsage The use case used to increment the usage count of the shortcut.
 */
class CreateTransactionFromShortcutUseCase @Inject constructor(
    private val insertTransaction: InsertTransactionUseCase,
    private val incrementShortcutUsage: IncrementShortcutUsageUseCase
) {
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
package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.utils.date.DateUtils
import javax.inject.Inject

/**
 * Use case responsible for creating and persisting a new [Transaction] based on a predefined [Shortcut].
 *
 * It maps the properties of a shortcut (amount, category, currency, etc.) to a new transaction
 * instance, setting the current date as the transaction date, and uses [InsertTransactionUseCase]
 * to save it to the data source.
 *
 * @property insertTransaction The use case used to persist the newly created transaction.
 */
class CreateTransactionFromShortcutUseCase @Inject constructor(
    private val insertTransaction: InsertTransactionUseCase
) {
    suspend operator fun invoke(shortcut: Shortcut) {
        val transaction = Transaction(
            id = 0,
            categoryId = shortcut.categoryId,
            subcategoryId = null,
            amount = shortcut.amount,
            currency = shortcut.currency,
            paymentMethod = shortcut.paymentMethod,
            type = shortcut.transactionType,
            date = DateUtils.currentDate,
            description = shortcut.name,
        )

        insertTransaction(transaction)
    }
}
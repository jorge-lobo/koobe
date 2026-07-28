package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.utils.date.DateUtils
import javax.inject.Inject

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
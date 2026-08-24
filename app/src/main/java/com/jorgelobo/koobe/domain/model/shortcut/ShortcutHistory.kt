package com.jorgelobo.koobe.domain.model.shortcut

import com.jorgelobo.koobe.domain.model.transaction.Transaction

data class ShortcutHistory(
    val shortcut: Shortcut,
    val transactionCount: Int,
    val totalAmount: Double,
    val transactions: List<Transaction>
)
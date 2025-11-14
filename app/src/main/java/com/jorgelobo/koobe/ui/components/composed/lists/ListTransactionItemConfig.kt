package com.jorgelobo.koobe.ui.components.composed.lists

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.transaction.Transaction

@Stable
data class ListTransactionItemConfig(
    val transaction: Transaction,
    val onClick: (() -> Unit)? = null
)
package com.jorgelobo.koobe.ui.screen.historic.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.toggles.TransactionToggle
import com.jorgelobo.koobe.ui.components.base.toggles.transactionToggleConfig
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

/**
 * A composable section that displays a toggle switch for filtering transactions by type within
 * the historic screen.
 *
 * @param transactionTypeSelected The currently active [TransactionType] filter.
 * @param onTransactionTypeChange Callback invoked when the selected [TransactionType] changes.
 */
@Composable
fun HistoricToggleSection(
    transactionTypeSelected: TransactionType,
    onTransactionTypeChange: (TransactionType) -> Unit
) {
    Box(
        modifier = Modifier.padding(horizontal = Spacing.Medium)
    ) {
        TransactionToggle(
            config = transactionToggleConfig(
                selected = transactionTypeSelected,
                onOptionSelected = onTransactionTypeChange
            )
        )
    }
}
package com.jorgelobo.koobe.ui.screen.categories.editor.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.toggles.TransactionToggle
import com.jorgelobo.koobe.ui.components.base.toggles.transactionToggleConfig

@Composable
fun CategoryEditorToggleSection(
    transactionTypeSelected: TransactionType,
    onTransactionTypeChange: (TransactionType) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        TransactionToggle(
            config = transactionToggleConfig(
                selected = transactionTypeSelected,
                onOptionSelected = onTransactionTypeChange
            )
        )
    }
}
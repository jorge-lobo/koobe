package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.shortcuts.manager.components.ShortcutManagerListSection
import com.jorgelobo.koobe.ui.screen.shortcuts.manager.components.ShortcutManagerToggleSection
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ShortcutManagerScreenUI(
    modifier: Modifier = Modifier,
    state: ShortcutManagerUiState,
    transactionTypeSelected: TransactionType,
    onTransactionTypeChanged: (TransactionType) -> Unit,
    onEditShortcut: (shortcutId: Int) -> Unit,
    onDeleteShortcut: (shortcutId: Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
    ) {
        ShortcutManagerToggleSection(
            transactionTypeSelected = transactionTypeSelected,
            onTransactionTypeChanged = onTransactionTypeChanged
        )

        ShortcutManagerListSection(
            isEmpty = state.shortcutItems.isEmpty(),
            shortcuts = state.shortcutItems,
            onEditShortcut = onEditShortcut,
            onDeleteShortcut = onDeleteShortcut
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewShortcutManagerScreen() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        ShortcutManagerScreenUI(
            state = ShortcutManagerUiState(
                transactionTypeSelected = TransactionType.EXPENSE,
                shortcutItems = emptyList(),
                deleteDialog = ConfirmationDialogState()
            ),
            transactionTypeSelected = TransactionType.EXPENSE,
            onTransactionTypeChanged = {},
            onEditShortcut = {},
            onDeleteShortcut = {}
        )
    }
}
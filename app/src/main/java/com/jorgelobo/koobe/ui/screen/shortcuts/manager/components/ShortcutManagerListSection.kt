package com.jorgelobo.koobe.ui.screen.shortcuts.manager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.composed.cards.CardShortcutItem
import com.jorgelobo.koobe.ui.components.composed.cards.CardShortcutItemConfig
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContent
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContentConfig
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.shortcuts.manager.ShortcutItemUi
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey2
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ShortcutManagerListSection(
    isEmpty: Boolean,
    shortcuts: List<ShortcutItemUi>,
    onEditShortcut: (shortcutId: Int) -> Unit,
    onDeleteShortcut: (shortcutId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isEmpty) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.Giant))

            EmptyStateContent(
                config = EmptyStateContentConfig(
                    message = stringResource(R.string.empty_headline_shortcuts),
                    icon = IconPack.EMPTY,
                    iconTint = LightThemeGrey2,
                    iconType = EmptyStateIconType.BACKGROUND
                )
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            items(
                items = shortcuts,
                key = { it.shortcut.id }
            ) { item ->
                CardShortcutItem(
                    config = CardShortcutItemConfig(
                        shortcut = item.shortcut,
                        category = item.category
                    ),
                    onEditClick = { onEditShortcut(item.shortcut.id) },
                    onDeleteClick = { onDeleteShortcut(item.shortcut.id) }
                )
            }
        }
    }
}
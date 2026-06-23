package com.jorgelobo.koobe.ui.mappers

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorUiState

fun ShortcutEditorUiState.toShortcut(
    config: ShortcutEditorConfig
): Shortcut {
    return Shortcut(
        id = when (config) {
            is ShortcutEditorConfig.Create -> 0
            is ShortcutEditorConfig.Edit -> config.shortcutId
        },
        categoryId = category.id,
        name = name.trim(),
        icon = icon,
        amount = amount,
        repeat = repeat,
        period = if (repeat) repeatFrequency else null,
        paymentMethod = paymentMethodType,
        currency = currencyType,
        transactionType = category.type
    )
}
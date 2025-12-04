package com.jorgelobo.koobe.ui.components.model.shortcut

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

data class ShortcutUiModel(
    val shortcut: Shortcut,
    val category: Category
) {
    companion object {
        fun from(shortcut: Shortcut, category: Category): ShortcutUiModel {
            return ShortcutUiModel(
                shortcut = shortcut,
                category = category
            )
        }
    }
}
package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

/**
 * UI representation of a shortcut item, combining the shortcut domain model with its associated
 * category information for display in the shortcut manager.
 *
 * @property shortcut The underlying [Shortcut] data.
 * @property category The [Category] linked to the shortcut.
 */
data class ShortcutItemUi(
    val shortcut: Shortcut,
    val category: Category
)
package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

data class ShortcutItemUi(
    val shortcut: Shortcut,
    val category: Category
)
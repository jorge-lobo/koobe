package com.jorgelobo.koobe.ui.components.composed.lists

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

@Stable
data class ListShortcutItemConfig(
    val shortcut: Shortcut,
    val category: Category
)
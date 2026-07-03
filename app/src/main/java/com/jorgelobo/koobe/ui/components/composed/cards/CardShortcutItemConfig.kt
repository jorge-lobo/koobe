package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

@Stable
data class CardShortcutItemConfig(
    val shortcut: Shortcut,
    val category: Category
)
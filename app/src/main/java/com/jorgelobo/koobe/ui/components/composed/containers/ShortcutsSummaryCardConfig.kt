package com.jorgelobo.koobe.ui.components.composed.containers

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel

@Stable
data class ShortcutsSummaryCardConfig(
    val items: List<ShortcutUiModel> = emptyList(),
    val onShortcutClick: (ShortcutUiModel) -> Unit,
    val onActionClick: () -> Unit
)
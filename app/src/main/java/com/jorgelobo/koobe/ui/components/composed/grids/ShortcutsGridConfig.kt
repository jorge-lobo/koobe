package com.jorgelobo.koobe.ui.components.composed.grids

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel

@Stable
data class ShortcutsGridConfig(
    val list: List<ShortcutUiModel> = emptyList(),
    val selectedShortcutId: Int? = null,
    val onShortcutClick: (Int) -> Unit
)
package com.jorgelobo.koobe.ui.components.composed.shortcuts

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel

@Stable
data class ShortcutSimpleItemConfig(
    val model: ShortcutUiModel,
    val onClick: (() -> Unit)? = null
)
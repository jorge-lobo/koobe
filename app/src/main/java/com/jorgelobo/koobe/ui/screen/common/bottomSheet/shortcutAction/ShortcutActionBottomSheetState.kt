package com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutAction

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.shortcut.Shortcut

sealed interface ShortcutActionBottomSheetState {
    data object Hidden : ShortcutActionBottomSheetState
    data class Visible(
        val shortcut: Shortcut,
        val category: Category
    ) : ShortcutActionBottomSheetState
}
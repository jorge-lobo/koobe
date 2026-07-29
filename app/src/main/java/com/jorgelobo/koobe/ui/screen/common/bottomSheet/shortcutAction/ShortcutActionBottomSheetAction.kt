package com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutAction

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

sealed interface ShortcutActionBottomSheetAction {

    object Dismiss : ShortcutActionBottomSheetAction

    data class Execute(
        val shortcut: Shortcut
    ) : ShortcutActionBottomSheetAction

    data class Edit(
        val shortcut: Shortcut
    ) : ShortcutActionBottomSheetAction

    data class Open(
        val shortcut: Shortcut,
        val category: Category
    ) : ShortcutActionBottomSheetAction
}
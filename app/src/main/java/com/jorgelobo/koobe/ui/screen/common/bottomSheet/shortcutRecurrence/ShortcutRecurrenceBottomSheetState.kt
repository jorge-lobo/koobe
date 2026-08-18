package com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

sealed interface ShortcutRecurrenceBottomSheetState {
    data object Hidden : ShortcutRecurrenceBottomSheetState
    data class Visible(
        val shortcut: Shortcut,
        val category: Category
    ) : ShortcutRecurrenceBottomSheetState
}
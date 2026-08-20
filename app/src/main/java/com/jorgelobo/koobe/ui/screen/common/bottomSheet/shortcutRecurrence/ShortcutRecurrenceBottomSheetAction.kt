package com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

sealed interface ShortcutRecurrenceBottomSheetAction {

    object Dismiss : ShortcutRecurrenceBottomSheetAction

    data class Change(
        val shortcut: Shortcut
    ) : ShortcutRecurrenceBottomSheetAction

    data class Disable(
        val shortcut: Shortcut
    ) : ShortcutRecurrenceBottomSheetAction

    data class Open(
        val shortcut: Shortcut,
        val category: Category
    ) : ShortcutRecurrenceBottomSheetAction
}
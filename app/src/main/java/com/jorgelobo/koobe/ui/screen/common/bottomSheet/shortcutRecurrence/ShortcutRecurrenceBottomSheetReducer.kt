package com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence

fun reduceShortcutRecurrenceBottomSheet(
    action: ShortcutRecurrenceBottomSheetAction
) : ShortcutRecurrenceBottomSheetState {
    return when (action) {

        is ShortcutRecurrenceBottomSheetAction.Open ->
            ShortcutRecurrenceBottomSheetState.Visible(
                shortcut = action.shortcut,
                category = action.category
            )

        is ShortcutRecurrenceBottomSheetAction.Dismiss,
        is ShortcutRecurrenceBottomSheetAction.Change,
        is ShortcutRecurrenceBottomSheetAction.Disable ->
            ShortcutRecurrenceBottomSheetState.Hidden
    }
}
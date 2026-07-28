package com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutAction

fun reduceShortcutActionBottomSheet(
    action: ShortcutActionBottomSheetAction
): ShortcutActionBottomSheetState {
    return when (action) {

        is ShortcutActionBottomSheetAction.Open ->
            ShortcutActionBottomSheetState.Visible(
                shortcut = action.shortcut,
                category = action.category
            )


        is ShortcutActionBottomSheetAction.Dismiss,
        is ShortcutActionBottomSheetAction.Execute,
        is ShortcutActionBottomSheetAction.Edit ->
            ShortcutActionBottomSheetState.Hidden
    }
}
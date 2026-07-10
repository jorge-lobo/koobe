package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import com.jorgelobo.koobe.domain.model.constants.enums.SortingType

fun List<ShortcutItemUi>.sortedBy(
    sorting: SortingType
): List<ShortcutItemUi> = when (sorting) {

    SortingType.NAME_ASC -> sortedBy { it.shortcut.name.lowercase() }

    SortingType.NAME_DESC -> sortedByDescending { it.shortcut.name.lowercase() }

    SortingType.CATEGORY -> sortedWith(
        compareBy(
            { it.category.name.lowercase() },
            { it.shortcut.name.lowercase() }
        )
    )

    SortingType.AMOUNT_ASC -> sortedBy { it.shortcut.amount }

    SortingType.AMOUNT_DESC -> sortedByDescending { it.shortcut.amount }
}
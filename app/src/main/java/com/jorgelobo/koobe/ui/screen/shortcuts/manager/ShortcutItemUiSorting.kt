package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import com.jorgelobo.koobe.domain.model.constants.enums.SortingType

/**
 * Sorts a list of [ShortcutItemUi] based on the provided [SortingType].
 *
 * The sorting can be performed by name (ascending/descending), by category, or by amount
 * (ascending/descending).
 *
 * @param sorting The criteria used to determine the order of the elements.
 * @return A new list containing the elements sorted according to the specified criteria.
 */
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
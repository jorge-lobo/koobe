package com.jorgelobo.koobe.ui.screen.categories.selector

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import kotlinx.serialization.Serializable

/**
 * Configuration object for initializing the Category Selector flow.
 *
 * This config defines how the selector behaves, what data is initially selected,
 * and where the final selection result should be routed.
 *
 * It is created at navigation time and consumed by the CategorySelectorViewModel
 * to initialize its state.
 *
 * @property mode Determines the UI and behavior of the selector.
 * @property target Defines the destination that will receive the selection result.
 *
 * @property transactionId Optional transaction ID used when editing an existing transaction.
 * @property subcategoryId Optional subcategory ID used when editing a subcategory.
 * @property shortcutId Optional shortcut ID used when editing a shortcut.
 * @property budgetId Optional budget ID used when editing a budget.
 *
 * @property initialTransactionType Initial transaction type selected when the screen opens.
 * @property initialCategoryId Optional category preselected on entry.
 * @property initialSubcategoryId Optional subcategory preselected on entry.
 * @property initialShortcutId Optional shortcut preselected on entry.
 */
@Serializable
data class CategorySelectorConfig(
    val mode: CategorySelectorMode,
    val target: CategorySelectorTarget,
    val transactionId: Int? = null,
    val subcategoryId: Int? = null,
    val shortcutId: Int? = null,
    val budgetId: Int? = null,
    val initialTransactionType: TransactionType,
    val initialCategoryId: Int? = null,
    val initialSubcategoryId: Int? = null,
    val initialShortcutId: Int? = null
)
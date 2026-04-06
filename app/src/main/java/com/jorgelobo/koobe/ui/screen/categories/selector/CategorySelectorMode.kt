package com.jorgelobo.koobe.ui.screen.categories.selector

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import kotlinx.serialization.Serializable

/**
 * Defines the behavior and UI configuration of the Category Selector
 * for different usage contexts.
 *
 * Each mode represents a specific entry point into the selector and controls:
 * - visual elements (headline, leading icon)
 * - selection requirements (category vs subcategory)
 * - visibility of interactive UI elements
 *
 * This enum serves as a single source of truth for adapting the selector
 * flow based on its invocation context.
 *
 * @property headlineRes String resource used as the screen headline
 * @property leadingIcon Icon displayed in the app bar leading action
 * @property showToggle Whether the transaction type toggle should be visible
 * @property requiresSubcategorySelection Whether a subcategory selection is required
 * @property showActionButton Whether the primary action button is shown
 * @property actionButtonLabelRes Optional label for the primary action button
 */
@Serializable
enum class CategorySelectorMode(
    val headlineRes: Int,
    val leadingIcon: IconPack,
    val showToggle: Boolean,
    val requiresSubcategorySelection: Boolean,
    val showActionButton: Boolean,
    val actionButtonLabelRes: Int? = null
) {
    // Transaction-related modes
    CREATE_TRANSACTION(
        headlineRes = R.string.headline_category_selector,
        leadingIcon = IconPack.CLOSE,
        showToggle = false,
        requiresSubcategorySelection = true,
        showActionButton = false,
        actionButtonLabelRes = null
    ),
    EDIT_TRANSACTION(
        headlineRes = R.string.headline_category_changer,
        leadingIcon = IconPack.BACK,
        showToggle = true,
        requiresSubcategorySelection = true,
        showActionButton = false,
        actionButtonLabelRes = null
    ),

    // Subcategory editing
    EDIT_SUBCATEGORY(
        headlineRes = R.string.headline_category_changer,
        leadingIcon = IconPack.BACK,
        showToggle = true,
        requiresSubcategorySelection = false,
        showActionButton = true,
        actionButtonLabelRes = R.string.btn_change
    ),

    // Shortcut-related modes
    CREATE_SHORTCUT(
        headlineRes = R.string.headline_category_selector,
        leadingIcon = IconPack.CLOSE,
        showToggle = true,
        requiresSubcategorySelection = false,
        showActionButton = true,
        actionButtonLabelRes = R.string.btn_continue
    ),
    EDIT_SHORTCUT(
        headlineRes = R.string.headline_category_changer,
        leadingIcon = IconPack.BACK,
        showToggle = true,
        requiresSubcategorySelection = false,
        showActionButton = true,
        actionButtonLabelRes = R.string.btn_change
    ),

    // Budget-related modes
    CREATE_BUDGET(
        headlineRes = R.string.headline_category_selector,
        leadingIcon = IconPack.CLOSE,
        showToggle = true,
        requiresSubcategorySelection = true,
        showActionButton = false,
        actionButtonLabelRes = null
    ),
    EDIT_BUDGET(
        headlineRes = R.string.headline_category_changer,
        leadingIcon = IconPack.BACK,
        showToggle = true,
        requiresSubcategorySelection = true,
        showActionButton = false,
        actionButtonLabelRes = null
    ),

    // Fallback / generic mode
    DEFAULT(
        headlineRes = R.string.headline_category_selector,
        leadingIcon = IconPack.BACK,
        showToggle = false,
        requiresSubcategorySelection = false,
        showActionButton = false,
        actionButtonLabelRes = null
    )
}
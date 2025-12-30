package com.jorgelobo.koobe.ui.screen.categories.selector

import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.R
import kotlinx.serialization.Serializable

@Serializable
enum class CategorySelectorMode(
    val headlineRes: Int,
    val leadingIcon: IconGeneral,
    val showToggle: Boolean,
    val requiresSubcategorySelection: Boolean,
    val showActionButton: Boolean,
    val actionButtonLabelRes: Int? = null
) {
    CREATE_TRANSACTION(
        headlineRes = R.string.headline_category_selector,
        leadingIcon = IconGeneral.CLOSE,
        showToggle = false,
        requiresSubcategorySelection = true,
        showActionButton = false,
        actionButtonLabelRes = null
    ),
    EDIT_TRANSACTION(
        headlineRes = R.string.headline_category_changer,
        leadingIcon = IconGeneral.BACK,
        showToggle = true,
        requiresSubcategorySelection = true,
        showActionButton = false,
        actionButtonLabelRes = null
    ),
    EDIT_SUBCATEGORY(
        headlineRes = R.string.headline_category_changer,
        leadingIcon = IconGeneral.BACK,
        showToggle = true,
        requiresSubcategorySelection = false,
        showActionButton = true,
        actionButtonLabelRes = R.string.btn_change
    ),
    CREATE_SHORTCUT(
        headlineRes = R.string.headline_category_selector,
        leadingIcon = IconGeneral.CLOSE,
        showToggle = true,
        requiresSubcategorySelection = false,
        showActionButton = true,
        actionButtonLabelRes = R.string.btn_continue
    ),
    EDIT_SHORTCUT(
        headlineRes = R.string.headline_category_changer,
        leadingIcon = IconGeneral.BACK,
        showToggle = true,
        requiresSubcategorySelection = false,
        showActionButton = true,
        actionButtonLabelRes = R.string.btn_change
    ),
    CREATE_BUDGET(
        headlineRes = R.string.headline_category_selector,
        leadingIcon = IconGeneral.CLOSE,
        showToggle = true,
        requiresSubcategorySelection = true,
        showActionButton = false,
        actionButtonLabelRes = null
    ),
    EDIT_BUDGET(
        headlineRes = R.string.headline_category_changer,
        leadingIcon = IconGeneral.BACK,
        showToggle = true,
        requiresSubcategorySelection = true,
        showActionButton = false,
        actionButtonLabelRes = null
    ),
    DEFAULT(
        headlineRes = R.string.headline_category_selector,
        leadingIcon = IconGeneral.BACK,
        showToggle = false,
        requiresSubcategorySelection = false,
        showActionButton = false,
        actionButtonLabelRes = null
    )
}
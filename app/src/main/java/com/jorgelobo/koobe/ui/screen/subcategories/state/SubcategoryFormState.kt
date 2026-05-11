package com.jorgelobo.koobe.ui.screen.subcategories.state

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class SubcategoryFormState(
    val name: FieldUpdate<String> = FieldUpdate.Unchanged,
    val icon: FieldUpdate<IconPack> = FieldUpdate.Unchanged,
    val categoryId: FieldUpdate<Int> = FieldUpdate.Unchanged
) {
    val hasChanges: Boolean
        get() = name is FieldUpdate.Updated ||
                icon is FieldUpdate.Updated ||
                categoryId is FieldUpdate.Updated
}
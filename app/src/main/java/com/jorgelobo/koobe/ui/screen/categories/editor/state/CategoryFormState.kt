package com.jorgelobo.koobe.ui.screen.categories.editor.state

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.core.model.FieldUpdate

data class CategoryFormState(
    val name: FieldUpdate<String> = FieldUpdate.Unchanged,
    val icon: FieldUpdate<IconPack> = FieldUpdate.Unchanged,
    val color: FieldUpdate<Color> = FieldUpdate.Unchanged,
    val type: FieldUpdate<TransactionType> = FieldUpdate.Unchanged,
    val subcategories: FieldUpdate<List<Subcategory>> = FieldUpdate.Unchanged
) {
    val hasChanges: Boolean
        get() = name is FieldUpdate.Updated ||
                icon is FieldUpdate.Updated ||
                color is FieldUpdate.Updated ||
                type is FieldUpdate.Updated ||
                subcategories is FieldUpdate.Updated
}
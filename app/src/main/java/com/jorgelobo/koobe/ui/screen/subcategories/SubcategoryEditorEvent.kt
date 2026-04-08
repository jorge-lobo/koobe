package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

sealed class SubcategoryEditorEvent {
    object NavigateBack : SubcategoryEditorEvent()
    data class NavigateTo(val route: String) : SubcategoryEditorEvent()
    data class ShowSnackBar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconPack? = null
    ) : SubcategoryEditorEvent()
}
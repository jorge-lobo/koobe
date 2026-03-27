package com.jorgelobo.koobe.ui.screen.subcategories

sealed class SubcategoryEditorEvent {
    object NavigateBack : SubcategoryEditorEvent()
    data class NavigateTo(val route: String) : SubcategoryEditorEvent()
}
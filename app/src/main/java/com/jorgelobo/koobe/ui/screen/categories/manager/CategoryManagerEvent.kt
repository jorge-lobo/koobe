package com.jorgelobo.koobe.ui.screen.categories.manager

sealed class CategoryManagerEvent {
    object NavigateBack : CategoryManagerEvent()
    data class NavigateTo(val route: String) : CategoryManagerEvent()
}
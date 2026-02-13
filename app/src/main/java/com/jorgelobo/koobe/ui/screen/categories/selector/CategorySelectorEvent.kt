package com.jorgelobo.koobe.ui.screen.categories.selector

/**
 * Sealed interface representing the possible UI events and navigation actions
 * from the Category Selector screen.
 */
sealed interface CategorySelectorEvent {
    object NavigateBack : CategorySelectorEvent
    data class NavigateTo(val route: String) : CategorySelectorEvent
    data class NavigateAndReplace(val route: String) : CategorySelectorEvent
}
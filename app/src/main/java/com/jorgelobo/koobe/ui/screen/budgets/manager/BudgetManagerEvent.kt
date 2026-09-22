package com.jorgelobo.koobe.ui.screen.budgets.manager

/**
 * Sealed class representing the possible UI events or navigation actions that can be triggered
 * from the Budget Manager screen.
 */
sealed class BudgetManagerEvent {
    data object NavigateBack : BudgetManagerEvent()
    data class NavigateTo(val route: String) : BudgetManagerEvent()
}
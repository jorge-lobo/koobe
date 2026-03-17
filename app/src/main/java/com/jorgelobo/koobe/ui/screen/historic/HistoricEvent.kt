package com.jorgelobo.koobe.ui.screen.historic

/**
 * Sealed class representing the possible UI events or navigation actions that can be triggered
 * from the Historic screen.
 */
sealed class HistoricEvent  {
    object NavigateBack : HistoricEvent()
    data class NavigateTo(val route: String) : HistoricEvent()
}
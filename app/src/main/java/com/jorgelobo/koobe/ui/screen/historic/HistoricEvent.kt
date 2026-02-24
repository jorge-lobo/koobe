package com.jorgelobo.koobe.ui.screen.historic

sealed class HistoricEvent  {
    object NavigateBack : HistoricEvent()
    data class NavigateTo(val route: String) : HistoricEvent()
}
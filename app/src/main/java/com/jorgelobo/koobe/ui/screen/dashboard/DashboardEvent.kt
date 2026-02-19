package com.jorgelobo.koobe.ui.screen.dashboard

/**
 * Represents the various user interactions or UI events that can occur
 * on the Dashboard screen.
 */
sealed class DashboardEvent {
    data class NavigateTo(val route: String) : DashboardEvent()
}
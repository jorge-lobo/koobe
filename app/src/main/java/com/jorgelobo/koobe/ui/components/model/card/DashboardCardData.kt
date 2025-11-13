package com.jorgelobo.koobe.ui.components.model.card

import androidx.compose.ui.graphics.vector.ImageVector

data class DashboardCardData(
    val label: String,
    val emptyStateIcon: ImageVector,
    val emptyStateMessage: String,
    val addButtonLabel: String
)
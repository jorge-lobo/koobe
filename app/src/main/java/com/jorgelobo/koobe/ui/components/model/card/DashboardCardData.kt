package com.jorgelobo.koobe.ui.components.model.card

import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class DashboardCardData(
    val label: String,
    val emptyStateIcon: IconPack,
    val emptyStateMessage: String,
    val addButtonLabel: String
)
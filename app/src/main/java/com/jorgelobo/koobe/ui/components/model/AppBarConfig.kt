package com.jorgelobo.koobe.ui.components.model

data class AppBarConfig(
    val headline: String,
    val leadingAction: AppBarAction,
    val trailingActions: List<AppBarAction> = emptyList()
)

data class AppBarAction(
    val icon: IconName,
    val onClick: () -> Unit
)
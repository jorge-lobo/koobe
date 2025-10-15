package com.jorgelobo.koobe.ui.components.composed.appBar

import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral

data class AppBarConfig(
    val headline: String,
    val leadingAction: AppBarAction,
    val trailingActions: List<AppBarAction> = emptyList()
)

data class AppBarAction(
    val icon: IconGeneral,
    val onClick: () -> Unit
)
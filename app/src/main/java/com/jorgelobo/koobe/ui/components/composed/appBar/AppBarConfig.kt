package com.jorgelobo.koobe.ui.components.composed.appBar

import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class AppBarConfig(
    val headline: String,
    val leadingAction: AppBarAction,
    val trailingActions: List<AppBarAction> = emptyList()
)

data class AppBarAction(
    val icon: IconPack,
    val onClick: () -> Unit
)
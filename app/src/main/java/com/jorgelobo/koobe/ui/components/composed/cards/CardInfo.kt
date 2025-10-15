package com.jorgelobo.koobe.ui.components.composed.cards

data class CardInfo(
    val title: String,
    val subtitle: String? = null,
    val amount: String? = null,
    val icon: String? = null,
    val onClick: (() -> Unit)? = null
)
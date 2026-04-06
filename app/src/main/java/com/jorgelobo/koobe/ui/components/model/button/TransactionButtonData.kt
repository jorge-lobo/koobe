package com.jorgelobo.koobe.ui.components.model.button

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class TransactionButtonData(
    val icon: IconPack,
    val color: Color,
    val text: String,
    val contextDescription: String
)
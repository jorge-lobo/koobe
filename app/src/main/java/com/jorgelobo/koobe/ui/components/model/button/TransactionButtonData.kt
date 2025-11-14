package com.jorgelobo.koobe.ui.components.model.button

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class TransactionButtonData(
    val icon: ImageVector,
    val color: Color,
    val text: String,
    val contextDescription: String
)
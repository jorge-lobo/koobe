package com.jorgelobo.koobe.ui.components.base.buttons.base

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

@Stable
data class ButtonConfig(
    val type: ButtonType,
    val state: UiState = UiState.ENABLED,
    val text: String,
    val textColor: Color? = null,
    val icon: IconPack? = null,
    val transactionType: TransactionType? = null,
    val onClick: () -> Unit
)
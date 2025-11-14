package com.jorgelobo.koobe.ui.components.base.buttons.base

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.icons.getIconFromName

@Stable
data class ButtonConfig(
    val type: ButtonType,
    val state: UiState = UiState.ENABLED,
    val text: String,
    val textColor: Color? = null,
    val icon: IconGeneral? = null,
    val transactionType: TransactionType? = null,
    val onClick: () -> Unit
)

fun ButtonConfig.iconVector(): ImageVector? =
    icon?.let { getIconFromName(it) }
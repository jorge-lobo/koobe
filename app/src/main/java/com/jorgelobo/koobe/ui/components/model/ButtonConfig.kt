package com.jorgelobo.koobe.ui.components.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.TransactionType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.icons.getIconFromName

enum class ButtonType { PRIMARY, SECONDARY, SECONDARY_COMPACT, SQUARE, TEXT, ADD_TRANSACTION }
enum class IconButtonType { APP_BAR, DISCLOSURE, INPUT, LIST_ITEM }
enum class ButtonState { ENABLED, DISABLED }

data class ButtonConfig(
    val type: ButtonType,
    val state: ButtonState = ButtonState.ENABLED,
    val text: String,
    val icon: IconGeneral? = null,
    val transactionType: TransactionType? = null,
    val onClick: () -> Unit
)

fun ButtonConfig.iconVector(): ImageVector? =
    icon?.let { getIconFromName(it) }
package com.jorgelobo.koobe.ui.components.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class SelectorType { TEXT, ICON, COLOR }
enum class SelectorWidth { SMALL, MEDIUM }

data class SelectorFieldConfig(
    val selectorType: SelectorType = SelectorType.TEXT,
    val width: SelectorWidth = SelectorWidth.SMALL,
    val label: String? = null,
    val value: String? = null,
    val icon: ImageVector? = null,
    val iconTint: Color? =null,
    val color: Color? = null,
    val onClick: () -> Unit
)
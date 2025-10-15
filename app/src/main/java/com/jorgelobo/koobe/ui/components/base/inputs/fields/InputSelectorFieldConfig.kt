package com.jorgelobo.koobe.ui.components.base.inputs.fields

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.enums.SelectorType
import com.jorgelobo.koobe.ui.components.model.enums.SelectorWidth

data class InputSelectorFieldConfig(
    val selectorType: SelectorType = SelectorType.TEXT,
    val width: SelectorWidth = SelectorWidth.SMALL,
    val label: String? = null,
    val value: String? = null,
    val icon: ImageVector? = null,
    val iconTint: Color? =null,
    val color: Color? = null,
    val onClick: () -> Unit
)
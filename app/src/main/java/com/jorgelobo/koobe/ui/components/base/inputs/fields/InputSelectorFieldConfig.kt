package com.jorgelobo.koobe.ui.components.base.inputs.fields

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.ui.components.model.enums.SelectorType
import com.jorgelobo.koobe.ui.components.model.enums.SelectorWidth
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class InputSelectorFieldConfig(
    val selectorType: SelectorType = SelectorType.TEXT,
    val width: SelectorWidth = SelectorWidth.SMALL,
    val label: String? = null,
    val value: String? = null,
    val icon: IconPack? = null,
    val iconTint: Color? =null,
    val color: Color? = null,
    val onClick: () -> Unit
)
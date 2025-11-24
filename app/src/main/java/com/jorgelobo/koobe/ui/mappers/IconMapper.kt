package com.jorgelobo.koobe.ui.mappers

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral

fun getIconFromName(name: IconGeneral): ImageVector {
    return name.icon
}
package com.jorgelobo.koobe.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral

fun getIconFromName(name: IconGeneral): ImageVector {
    return name.icon
}
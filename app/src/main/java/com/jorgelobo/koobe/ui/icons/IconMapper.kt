package com.jorgelobo.koobe.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.IconName

fun getIconFromName(name: IconName): ImageVector {
    return name.icon
}
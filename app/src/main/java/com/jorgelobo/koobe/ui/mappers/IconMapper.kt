package com.jorgelobo.koobe.ui.mappers

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

fun getIconFromName(name: IconGeneral): ImageVector {
    return name.icon
}

fun getIconFromString(name: String?): ImageVector {
    return try {
        IconPack.valueOf(name ?: "").icon
    } catch (e: IllegalArgumentException) {
        IconPack.MISCELLANEOUS.icon
    }
}
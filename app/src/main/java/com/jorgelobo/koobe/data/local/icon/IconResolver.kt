package com.jorgelobo.koobe.data.local.icon

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

object IconResolver {

    private val map = IconPack.entries.associateBy { it.name }

    fun resolve(name: String): ImageVector {
        return map[name]?.icon ?: IconPack.EXTRA.icon
    }

    fun findIconPackByVector(icon: ImageVector): IconPack? {
        return IconPack.entries.firstOrNull { it.icon == icon }
    }
}
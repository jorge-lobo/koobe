package com.jorgelobo.koobe.ui.components.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.adamglin.phosphoricons.RegularGroup
import com.adamglin.phosphoricons.regular.ArrowsLeftRight
import com.adamglin.phosphoricons.regular.CaretDown
import com.adamglin.phosphoricons.regular.CaretRight
import com.adamglin.phosphoricons.regular.CaretUp
import com.adamglin.phosphoricons.regular.PencilSimple
import com.adamglin.phosphoricons.regular.Plus
import com.adamglin.phosphoricons.regular.Trash
import com.adamglin.phosphoricons.regular.X
import com.adamglin.phosphoricons.regular.XCircle

enum class IconName(val icon: ImageVector) {
    BACK(RegularGroup.ArrowsLeftRight),
    CLOSE(RegularGroup.X),
    DELETE(RegularGroup.Trash),
    EDIT(RegularGroup.PencilSimple),
    DISCLOSURE(RegularGroup.CaretRight),
    EXPAND(RegularGroup.CaretDown),
    COLLAPSE(RegularGroup.CaretUp),
    RESET(RegularGroup.XCircle),
    CHANGE(RegularGroup.ArrowsLeftRight),
    ADD(RegularGroup.Plus)
}
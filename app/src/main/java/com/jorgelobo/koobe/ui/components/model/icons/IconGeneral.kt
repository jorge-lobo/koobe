package com.jorgelobo.koobe.ui.components.model.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.adamglin.phosphoricons.RegularGroup
import com.adamglin.phosphoricons.regular.ArrowLeft
import com.adamglin.phosphoricons.regular.ArrowsLeftRight
import com.adamglin.phosphoricons.regular.Backspace
import com.adamglin.phosphoricons.regular.CalendarDots
import com.adamglin.phosphoricons.regular.CaretDown
import com.adamglin.phosphoricons.regular.CaretRight
import com.adamglin.phosphoricons.regular.CaretUp
import com.adamglin.phosphoricons.regular.Empty
import com.adamglin.phosphoricons.regular.Faders
import com.adamglin.phosphoricons.regular.Minus
import com.adamglin.phosphoricons.regular.PencilSimple
import com.adamglin.phosphoricons.regular.Plus
import com.adamglin.phosphoricons.regular.SortAscending
import com.adamglin.phosphoricons.regular.Trash
import com.adamglin.phosphoricons.regular.Wallet
import com.adamglin.phosphoricons.regular.Wrench
import com.adamglin.phosphoricons.regular.X
import com.adamglin.phosphoricons.regular.XCircle

enum class IconGeneral(val icon: ImageVector) {
    BACK(RegularGroup.ArrowLeft),
    CLOSE(RegularGroup.X),
    DELETE(RegularGroup.Trash),
    EDIT(RegularGroup.PencilSimple),
    DISCLOSURE(RegularGroup.CaretRight),
    EXPAND(RegularGroup.CaretDown),
    COLLAPSE(RegularGroup.CaretUp),
    RESET(RegularGroup.XCircle),
    CHANGE(RegularGroup.ArrowsLeftRight),
    ADD(RegularGroup.Plus),
    MINUS(RegularGroup.Minus),
    FILTER(RegularGroup.Faders),
    SETTINGS(RegularGroup.Wrench),
    SORT(RegularGroup.SortAscending),
    BACKSPACE(RegularGroup.Backspace),
    CALENDAR(RegularGroup.CalendarDots),
    EMPTY(RegularGroup.Empty),
    WALLET(RegularGroup.Wallet)
}
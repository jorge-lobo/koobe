package com.jorgelobo.koobe.ui.components.model.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.adamglin.phosphoricons.RegularGroup
import com.adamglin.phosphoricons.regular.Briefcase
import com.adamglin.phosphoricons.regular.Car
import com.adamglin.phosphoricons.regular.CashRegister
import com.adamglin.phosphoricons.regular.ChartPieSlice
import com.adamglin.phosphoricons.regular.Circuitry
import com.adamglin.phosphoricons.regular.CoatHanger
import com.adamglin.phosphoricons.regular.Coins
import com.adamglin.phosphoricons.regular.CookingPot
import com.adamglin.phosphoricons.regular.ExclamationMark
import com.adamglin.phosphoricons.regular.GameController
import com.adamglin.phosphoricons.regular.GraduationCap
import com.adamglin.phosphoricons.regular.Handshake
import com.adamglin.phosphoricons.regular.Heartbeat
import com.adamglin.phosphoricons.regular.HouseLine
import com.adamglin.phosphoricons.regular.Invoice
import com.adamglin.phosphoricons.regular.PawPrint
import com.adamglin.phosphoricons.regular.PiggyBank
import com.adamglin.phosphoricons.regular.ShoppingCart
import com.adamglin.phosphoricons.regular.Shower
import com.adamglin.phosphoricons.regular.SoccerBall
import com.adamglin.phosphoricons.regular.SuitcaseRolling
import com.adamglin.phosphoricons.regular.UsersFour

enum class IconCategory(val icon: ImageVector) {
    APPAREL(RegularGroup.CoatHanger),
    BODY_CARE(RegularGroup.Shower),
    DINING(RegularGroup.CookingPot),
    EDUCATION(RegularGroup.GraduationCap),
    ENTERTAINMENT(RegularGroup.GameController),
    ESSENTIALS(RegularGroup.Invoice),
    EXTRA(RegularGroup.ExclamationMark),
    FAMILY(RegularGroup.UsersFour),
    GROCERY(RegularGroup.ShoppingCart),
    HEALTH(RegularGroup.Heartbeat),
    HOME(RegularGroup.HouseLine),
    INVESTMENTS(RegularGroup.ChartPieSlice),
    MISCELLANEOUS(RegularGroup.Coins),
    PASSIVE(RegularGroup.PiggyBank),
    PETS(RegularGroup.PawPrint),
    SALES(RegularGroup.CashRegister),
    SPORTS(RegularGroup.SoccerBall),
    SUPPORT(RegularGroup.Handshake),
    TECHNOLOGY(RegularGroup.Circuitry),
    TRANSPORTATION(RegularGroup.Car),
    TRAVEL(RegularGroup.SuitcaseRolling),
    WORK(RegularGroup.Briefcase)
}
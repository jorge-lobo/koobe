package com.jorgelobo.koobe.ui.components.model.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.adamglin.phosphoricons.RegularGroup
import com.adamglin.phosphoricons.regular.*

enum class IconPayment(val icon: ImageVector) {
    CASH(RegularGroup.Coins),
    CARD(RegularGroup.CreditCard),
    TRANSFER(RegularGroup.ArrowsLeftRight),
    CRYPTO(RegularGroup.CurrencyBtc)
}
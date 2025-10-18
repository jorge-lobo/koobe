package com.jorgelobo.koobe.ui.components.composed.containers

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.model.enums.ScreenType

data class BalanceContainerConfig(
    val balance: Double,
    val income: Double,
    val expenses: Double,
    val currencyType: CurrencyType,
    val screenType: ScreenType
)
package com.jorgelobo.koobe.ui.screen.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.composed.appBar.LogoAppBar
import com.jorgelobo.koobe.ui.components.composed.containers.BalanceContainer
import com.jorgelobo.koobe.ui.components.composed.containers.BalanceContainerConfig
import com.jorgelobo.koobe.ui.components.composed.date.DateDisplay
import com.jorgelobo.koobe.ui.components.composed.date.DateDisplayConfig
import com.jorgelobo.koobe.ui.components.model.enums.ScreenType
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import java.util.Date

@Composable
fun DashboardTopSection(
    date: Date,
    currencyType: CurrencyType,
    overallBalance: Double,
    income: Double,
    expenses: Double
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Spacer(modifier = Modifier.height(Spacing.Small))

        DateDisplay(
            config = DateDisplayConfig(
                periodType = PeriodType.WEEKLY,
                date = date
            )
        )

        BalanceContainer(
            config = BalanceContainerConfig(
                balance = overallBalance,
                income = income,
                expenses = expenses,
                currencyType = currencyType,
                screenType = ScreenType.DASHBOARD
            ),
            modifier = Modifier.padding(horizontal = Spacing.Medium)
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))
    }
}
package com.jorgelobo.koobe.ui.screen.historic.components

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
import com.jorgelobo.koobe.ui.components.composed.containers.BalanceContainer
import com.jorgelobo.koobe.ui.components.composed.containers.BalanceContainerConfig
import com.jorgelobo.koobe.ui.components.composed.date.DateDisplay
import com.jorgelobo.koobe.ui.components.composed.date.DateDisplayConfig
import com.jorgelobo.koobe.ui.components.model.enums.ScreenType
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import java.util.Date

/**
 * Composable that represents the top section of the historic screen.
 * It displays the current period date information and a summary of the financial balance.
 *
 * @param date The reference date to be displayed.
 * @param periodType The type of period (e.g., Daily, Monthly) used to format the date display.
 * @param currencyType The currency format to be applied to the financial values.
 * @param balance The calculated net balance for the given period.
 * @param income The total income amount for the given period.
 * @param expenses The total expenses amount for the given period.
 */
@Composable
fun HistoricTopSection(
    date: Date,
    periodType: PeriodType,
    currencyType: CurrencyType,
    balance: Double,
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
                periodType = periodType,
                date = date
            )
        )

        BalanceContainer(
            config = BalanceContainerConfig(
                balance = balance,
                income = income,
                expenses = expenses,
                currencyType = currencyType,
                screenType = ScreenType.HISTORIC
            ),
            modifier = Modifier.padding(horizontal = Spacing.Medium)
        )
    }
}
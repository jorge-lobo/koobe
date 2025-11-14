package com.jorgelobo.koobe.ui.components.composed.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.DateFormat
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils
import com.jorgelobo.koobe.utils.DateUtils.formatAs
import java.util.Date

@Composable
fun FormattedDateText(
    periodType: PeriodType,
    date: Date
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text

    val formatedDate = when (periodType) {
        PeriodType.YEARLY -> date.formatAs(DateFormat.YEAR)
        PeriodType.MONTHLY -> date.formatAs(DateFormat.YEAR)
        PeriodType.WEEKLY -> date.formatAs(DateFormat.MONTH_YEAR)
        PeriodType.DAILY -> date.formatAs(DateFormat.DAY_MONTH_YEAR)
    }

    Text(
        text = formatedDate,
        style = typography.titleSmall,
        color = colors.textColors.textSecondary,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun DateDisplay(
    modifier: Modifier = Modifier,
    config: DateDisplayConfig
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        FormattedDateText(
            periodType = config.periodType,
            date = config.date
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewDateRow() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            val date = DateUtils.currentDate

            DateDisplay(
                config = DateDisplayConfig(
                    periodType = PeriodType.YEARLY,
                    date = date
                )
            )

            DateDisplay(
                config = DateDisplayConfig(
                    periodType = PeriodType.MONTHLY,
                    date = date
                )
            )

            DateDisplay(
                config = DateDisplayConfig(
                    periodType = PeriodType.WEEKLY,
                    date = date
                )
            )

            DateDisplay(
                config = DateDisplayConfig(
                    periodType = PeriodType.DAILY,
                    date = date
                )
            )
        }
    }
}
package com.jorgelobo.koobe.ui.components.composed.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonNavLeft
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonNavRight
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Height
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils

@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    config: DateSelectorConfig
) {
    val colors = AppTheme.colors
    val isDatePicker = config.periodType == PeriodType.DAILY
    val isRightEnabled = !DateUtils.isSameDay(config.date, DateUtils.currentDate)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Height.DateRow)
            .padding(horizontal = Spacing.Giant),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isDatePicker) {
            Spacer(modifier = Modifier.size(Spacing.Medium))
        } else {
            ButtonNavLeft(
                onClick = config.onLeftClick,
                enabled = true
            )
        }

        FormattedDateText(
            periodType = config.periodType,
            date = config.date
        )

        if (isDatePicker) {
            IconButton(
                modifier = Modifier.size(IconSize.Medium),
                onClick = config.onPickerClick
            ) {
                Icon(
                    imageVector = IconGeneral.CALENDAR.icon,
                    contentDescription = "Date Picker",
                    tint = colors.iconColors.iconPrimary,
                    modifier = Modifier.size(IconSize.Medium)
                )
            }
        } else {
            ButtonNavRight(
                onClick = config.onRightClick,
                enabled = isRightEnabled
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewDateSelector() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            val date = DateUtils.currentDate

            DateSelector(
                config = DateSelectorConfig(
                    periodType = PeriodType.YEARLY,
                    date = date,
                    onLeftClick = {},
                    onRightClick = {},
                    onPickerClick = {}
                ),
                modifier = Modifier
            )

            DateSelector(
                config = DateSelectorConfig(
                    periodType = PeriodType.MONTHLY,
                    date = date,
                    onLeftClick = {},
                    onRightClick = {},
                    onPickerClick = {}
                ),
                modifier = Modifier
            )

            DateSelector(
                config = DateSelectorConfig(
                    periodType = PeriodType.WEEKLY,
                    date = date,
                    onLeftClick = {},
                    onRightClick = {},
                    onPickerClick = {}
                ),
                modifier = Modifier
            )

            DateSelector(
                config = DateSelectorConfig(
                    periodType = PeriodType.DAILY,
                    date = date,
                    onLeftClick = {},
                    onRightClick = {},
                    onPickerClick = {}
                ),
                modifier = Modifier
            )
        }
    }
}
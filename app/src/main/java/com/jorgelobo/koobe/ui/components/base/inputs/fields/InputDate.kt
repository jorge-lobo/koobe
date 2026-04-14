package com.jorgelobo.koobe.ui.components.base.inputs.fields

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.InputSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.date.DateUtils

@Composable
fun InputDate(
    config: InputDateConfig,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val shape = AppTheme.shapes.medium

    Box(
        modifier = modifier
            .height(InputSize.InputHeight)
            .clip(shape)
            .background(colors.containerColors.containerPrimary)
            .border(BorderDimens.Base, colors.containerColors.containerOutline, shape)
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = config.dateLabel,
                style = AppTheme.typography.text.bodySmall,
                color = colors.textColors.textPrimary,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(Spacing.Small))

            IconButton(onClick = config.onIconClick) {
                Icon(
                    imageVector = IconPack.CALENDAR.icon,
                    contentDescription = stringResource(R.string.cd_calendar),
                    tint = colors.iconColors.iconPrimary,
                    modifier = Modifier.size(IconSize.Medium)
                )
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewInputDate() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            InputDate(
                config = InputDateConfig(
                    date = DateUtils.currentDate,
                    onIconClick = {}
                )
            )
        }
    }
}
package com.jorgelobo.koobe.ui.components.base.inputs

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.InputDateConfig
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.icons.getIconFromName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.InputSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils

@Composable
fun InputDate(
    config: InputDateConfig,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val shapes = AppTheme.shapes

    Box(
        modifier = modifier
            .height(InputSize.InputHeight)
            .clip(shapes.medium)
            .background(colors.containerColors.containerPrimary)
            .border(1.dp, colors.containerColors.containerOutline, shapes.medium)
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
                    imageVector = getIconFromName(IconGeneral.CALENDAR),
                    contentDescription = null,
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
    KoobeTheme {
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
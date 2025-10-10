package com.jorgelobo.koobe.ui.components.base.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.SelectorFieldConfig
import com.jorgelobo.koobe.ui.components.model.SelectorType
import com.jorgelobo.koobe.ui.components.model.SelectorWidth
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.components.model.icons.IconPayment
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.SelectorSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseSelectorField(
    modifier: Modifier = Modifier,
    config: SelectorFieldConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val shape = AppTheme.shapes.small

    val icon = when (config.width) {
        SelectorWidth.MEDIUM -> IconGeneral.EXPAND.icon
        SelectorWidth.SMALL -> IconGeneral.DISCLOSURE.icon
    }
    val width = when (config.width) {
        SelectorWidth.SMALL -> SelectorSize.SmallWidth
        SelectorWidth.MEDIUM -> SelectorSize.MediumWidth
    }

    BaseFieldContainer(
        label = config.label,
        width = width,
        height = SelectorSize.SelectorFieldHeight,
        modifier = modifier
    ) {
        when (config.selectorType) {
            SelectorType.TEXT -> {
                config.value?.let {
                    Text(
                        text = it,
                        style = typography.bodyLarge,
                        color = colors.textColors.textPrimary
                    )
                }
            }

            SelectorType.ICON -> {
                config.icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = config.iconTint ?: colors.iconColors.iconPrimary,
                        modifier = Modifier.size(IconSize.Small)
                    )
                }
            }

            SelectorType.COLOR -> {
                config.color?.let {
                    Box(
                        modifier = Modifier
                            .size(IconSize.ExtraSmall)
                            .clip(shape)
                            .background(it, shape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        AnimatedIconButton(
            imageVector = icon,
            contentDescription = null,
            onClick = config.onClick
        )
    }
}

@Composable
fun SelectorPayment(
    onClick: () -> Unit,
    icon: ImageVector
) {
    BaseSelectorField(
        config = SelectorFieldConfig(
            width = SelectorWidth.SMALL,
            selectorType = SelectorType.ICON,
            icon = icon,
            iconTint = AppTheme.colors.iconColors.iconPrimary,
            onClick = onClick
        )
    )
}

@Composable
fun SelectorCurrency(
    onClick: () -> Unit,
    value: String
) {
    BaseSelectorField(
        config = SelectorFieldConfig(
            width = SelectorWidth.SMALL,
            selectorType = SelectorType.TEXT,
            value = value,
            onClick = onClick
        )
    )
}

@Composable
fun SelectorIcon(
    onClick: () -> Unit,
    icon: ImageVector,
    iconTint: Color
) {
    BaseSelectorField(
        config = SelectorFieldConfig(
            width = SelectorWidth.SMALL,
            selectorType = SelectorType.ICON,
            label = stringResource(R.string.label_icon),
            icon = icon,
            iconTint = iconTint,
            onClick = onClick
        )
    )
}

@Composable
fun SelectorColor(
    onClick: () -> Unit,
    color: Color
) {
    BaseSelectorField(
        config = SelectorFieldConfig(
            width = SelectorWidth.SMALL,
            selectorType = SelectorType.COLOR,
            label = stringResource(R.string.label_color),
            color = color,
            onClick = onClick
        )
    )
}

@Composable
fun SelectorPeriod(
    onClick: () -> Unit,
    value: String
) {
    BaseSelectorField(
        config = SelectorFieldConfig(
            width = SelectorWidth.MEDIUM,
            selectorType = SelectorType.TEXT,
            value = value,
            onClick = onClick
        )
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSelectorFields() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            SelectorPayment(
                onClick = {},
                icon = IconPayment.CASH.icon
            )

            SelectorCurrency(
                onClick = {},
                value = "EUR"
            )

            SelectorIcon(
                onClick = {},
                icon = IconGeneral.CALENDAR.icon,
                iconTint = AccentGold
            )

            SelectorColor(
                onClick = {},
                color = AccentGold
            )

            SelectorPeriod(
                onClick = {},
                value = "Monthly"
            )
        }
    }
}
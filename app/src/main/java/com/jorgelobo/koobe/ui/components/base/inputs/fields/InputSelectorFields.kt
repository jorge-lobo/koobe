package com.jorgelobo.koobe.ui.components.base.inputs.fields

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.inputs.base.AnimatedIconButton
import com.jorgelobo.koobe.ui.components.base.inputs.base.BaseFieldContainer
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.SelectorType
import com.jorgelobo.koobe.ui.components.model.enums.SelectorWidth
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.SelectorSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseSelectorField(
    modifier: Modifier = Modifier,
    config: InputSelectorFieldConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val shape = AppTheme.shapes.small

    val (icon, width, contentDescription) = when (config.width) {
        SelectorWidth.SMALL -> Triple(
            IconPack.DISCLOSURE,
            SelectorSize.SmallWidth,
            stringResource(R.string.cd_disclosure)
        )

        SelectorWidth.MEDIUM -> Triple(
            IconPack.EXPAND,
            SelectorSize.MediumWidth,
            stringResource(R.string.cd_expand)
        )
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
                        imageVector = it.icon,
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
            icon = icon,
            contentDescription = contentDescription,
            onClick = config.onClick
        )
    }
}

@Composable
fun SelectorPayment(
    onClick: () -> Unit,
    icon: IconPack
) {
    BaseSelectorField(
        config = InputSelectorFieldConfig(
            width = SelectorWidth.SMALL,
            selectorType = SelectorType.ICON,
            icon = icon,
            iconTint = AppTheme.colors.iconColors.iconPaymentMethod,
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
        config = InputSelectorFieldConfig(
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
    icon: IconPack,
    iconTint: Color
) {
    BaseSelectorField(
        config = InputSelectorFieldConfig(
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
        config = InputSelectorFieldConfig(
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
        config = InputSelectorFieldConfig(
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
            SelectorPayment(
                onClick = {},
                icon = IconPack.CASH
            )

            SelectorCurrency(
                onClick = {},
                value = "EUR"
            )

            SelectorIcon(
                onClick = {},
                icon = IconPack.CALENDAR,
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
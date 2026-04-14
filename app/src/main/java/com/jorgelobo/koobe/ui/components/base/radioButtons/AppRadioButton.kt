package com.jorgelobo.koobe.ui.components.base.radioButtons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.UiLabel
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    icon: IconPack? = null,
    label: String
) {
    val colors = AppTheme.colors.radioButtonColors

    val radioButtonColors = RadioButtonDefaults.colors(
        selectedColor = colors.radioButtonSelectedIcon,
        unselectedColor = colors.radioButtonUnselectedIcon
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            enabled = enabled,
            onClick = onClick,
            colors = radioButtonColors
        )

        Spacer(modifier = Modifier.width(Spacing.Medium))

        if (icon != null) {
            Icon(
                imageVector = icon.icon,
                modifier = Modifier.size(IconSize.ExtraSmall),
                contentDescription = label,
                tint = AppTheme.colors.iconColors.iconPaymentMethod
            )

            Spacer(modifier = Modifier.width(Spacing.Medium))
        }

        Text(
            text = label,
            style = AppTheme.typography.text.titleMedium,
            color = AppTheme.colors.textColors.textPrimary
        )
    }
}

@Composable
fun <T> RadioGroup(
    config: RadioButtonConfig<T>,
    modifier: Modifier = Modifier
) where T : Enum<T>, T : UiLabel {
    val enabled = config.state == UiState.ENABLED
    var selected by remember { mutableStateOf(config.selectedOption) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        config.options.forEach { option ->
            AppRadioButton(
                enabled = enabled,
                selected = selected == option,
                label = stringResource(option.toLabel()),
                icon = config.icons[option],
                onClick = {
                    if (enabled) {
                        selected = option
                        config.onSelectionChanged(option)
                    }
                }
            )
        }
    }
}

@Composable
fun PaymentMethodRadioButton(
    config: RadioButtonConfig<PaymentMethodType>
) {
    RadioGroup(config = config)
}

@Composable
fun CurrencyRadioButton(
    config: RadioButtonConfig<CurrencyType>
) {
    RadioGroup(config = config)
}

@Composable
fun StartOfWeekRadioButton(
    config: RadioButtonConfig<StartOfWeek>
) {
    RadioGroup(config = config)
}

@Composable
fun LanguageRadioButton(
    config: RadioButtonConfig<AppLanguage>
) {
    RadioGroup(config = config)
}

@Composable
fun PeriodRadioButton(
    config: RadioButtonConfig<PeriodType>
) {
    RadioGroup(config = config)
}

@Composable
fun PeriodFilterRadioButton(
    config: RadioButtonConfig<PeriodType>
) {
    RadioGroup(config = config)
}

@Composable
fun SortingRadioButton(
    config: RadioButtonConfig<SortingType>
) {
    RadioGroup(config = config)
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewRadioButtons() {
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
            var paymentMethodSelected by remember { mutableStateOf(PaymentMethodType.CASH) }
            var currencySelected by remember { mutableStateOf(CurrencyType.EUR) }
            var startOfWeekSelected by remember { mutableStateOf(StartOfWeek.SUNDAY) }
            var languageSelected by remember { mutableStateOf(AppLanguage.ENGLISH) }
            var periodSelected by remember { mutableStateOf(PeriodType.MONTHLY) }

            PaymentMethodRadioButton(
                config = paymentMethodRadioButtonConfig(
                    selected = paymentMethodSelected,
                    onOptionSelected = { paymentMethodSelected = it }
                )
            )

            CurrencyRadioButton(
                config = currencyRadioButtonConfig(
                    selected = currencySelected,
                    onOptionSelected = { currencySelected = it }
                )
            )

            StartOfWeekRadioButton(
                config = startOfWeekRadioButtonConfig(
                    selected = startOfWeekSelected,
                    onOptionSelected = { startOfWeekSelected = it }
                )
            )

            LanguageRadioButton(
                config = languageRadioButtonConfig(
                    selected = languageSelected,
                    onOptionSelected = { languageSelected = it }
                )
            )

            PeriodRadioButton(
                config = periodRadioButtonConfig(
                    selected = periodSelected,
                    onOptionSelected = { periodSelected = it }
                )
            )
        }
    }
}
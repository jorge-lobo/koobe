package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.dialogs.BaseDialog
import com.jorgelobo.koobe.ui.components.base.radioButtons.CurrencyRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.LanguageRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.PeriodFilterRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.StartOfWeekRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.currencyRadioButtonConfig
import com.jorgelobo.koobe.ui.components.base.radioButtons.languageRadioButtonConfig
import com.jorgelobo.koobe.ui.components.base.radioButtons.periodFilterRadioButtonConfig
import com.jorgelobo.koobe.ui.components.base.radioButtons.startOfWeekRadioButtonConfig
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.OptionSelectorType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.DialogSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun OptionSelectorDialog(
    modifier: Modifier = Modifier,
    config: OptionSelectorDialogConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text

    var enable by remember { mutableStateOf(false) }

    val (height, content) = when (config.type) {
        OptionSelectorType.CURRENCY -> Pair(
            DialogSize.OptionSelector.TripleOptions,
            @Composable {
                CurrencyRadioButton(
                    config = currencyRadioButtonConfig(
                        selected = config.selectedCurrency ?: CurrencyType.EUR,
                        onOptionSelected = {
                            config.onCurrencySelected(it)
                            enable = true
                        }
                    )
                )
            }
        )

        OptionSelectorType.FIRST_WEEKDAY -> Pair(
            DialogSize.OptionSelector.PairOptions,
            @Composable {
                StartOfWeekRadioButton(
                    config = startOfWeekRadioButtonConfig(
                        selected = config.selectedWeekday ?: StartOfWeek.SUNDAY,
                        onOptionSelected = {
                            config.onWeekdaySelected(it)
                            enable = true
                        }
                    )
                )
            }
        )

        OptionSelectorType.LANGUAGE -> Pair(
            DialogSize.OptionSelector.PairOptions,
            @Composable {
                LanguageRadioButton(
                    config = languageRadioButtonConfig(
                        selected = config.selectedLanguage ?: AppLanguage.ENGLISH,
                        onOptionSelected = {
                            config.onLanguageSelected(it)
                            enable = true
                        }
                    )
                )
            }
        )

        OptionSelectorType.PERIOD -> Pair(
            DialogSize.OptionSelector.PairOptions,
            @Composable {
                PeriodFilterRadioButton(
                    config = periodFilterRadioButtonConfig(
                        selected = config.selectedPeriod ?: PeriodType.MONTHLY,
                        onOptionSelected = {
                            config.onPeriodSelected(it)
                            enable = true
                        }
                    )
                )
            }
        )
    }

    BaseDialog(
        modifier = modifier,
        height = height,
        confirmText = stringResource(R.string.btn_apply),
        enable = enable,
        onDismissRequest = {},
        onConfirm = config.onConfirm,
        onCancel = config.onCancel
    ) {
        Text(
            text = config.title,
            style = typography.titleLarge,
            color = colors.textColors.textSecondary
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        content()
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCurrencyDialog() = PreviewOptionSelectorDialog(OptionSelectorType.CURRENCY)
@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewLanguageDialog() = PreviewOptionSelectorDialog(OptionSelectorType.LANGUAGE)
@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewFirstWeekdayDialog() = PreviewOptionSelectorDialog(OptionSelectorType.FIRST_WEEKDAY)
@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewPeriodDialog() = PreviewOptionSelectorDialog(OptionSelectorType.PERIOD)

@Composable
fun PreviewOptionSelectorDialog(type: OptionSelectorType) {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            OptionSelectorDialog(
                config = OptionSelectorDialogConfig(
                    type = type,
                    title = type.name,
                    onConfirm = {},
                    onCancel = {},
                    onCurrencySelected = {},
                    onLanguageSelected = {},
                    onWeekdaySelected = {},
                    onPeriodSelected = {}
                )
            )
        }
    }
}
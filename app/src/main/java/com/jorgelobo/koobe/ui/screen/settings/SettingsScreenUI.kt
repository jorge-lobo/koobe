package com.jorgelobo.koobe.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputSelector
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputSelectorConfig
import com.jorgelobo.koobe.ui.components.base.toggles.ThemeToggle
import com.jorgelobo.koobe.ui.components.base.toggles.themeToggleConfig
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.getCurrencyCode

@Composable
fun SettingsScreenUI(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    themeSelected: ThemeOption,
    onThemeOptionChange: (ThemeOption) -> Unit,
    onLanguageSelectorClick: () -> Unit,
    onCurrencySelectorClick: () -> Unit,
    onStartOfWeekSelectorClick: () -> Unit,
    onPaymentMethodSelectorClick: () -> Unit,
    onManageCategoriesClick: () -> Unit,
    onManageShortcutsClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.MediumLarge),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        SettingsToggleSection(
            themeSelected = themeSelected,
            onOptionSelected = onThemeOptionChange
        )

        SettingsSelectorSection(
            language = uiState.languageSelected,
            currencyType = uiState.currencySelected,
            startOfWeek = uiState.startOfWeekSelected,
            paymentMethod = uiState.paymentMethodSelected,
            onLanguageSelectorClick = onLanguageSelectorClick,
            onCurrencySelectorClick = onCurrencySelectorClick,
            onStartOfWeekSelectorClick = onStartOfWeekSelectorClick,
            onPaymentMethodSelectorClick = onPaymentMethodSelectorClick
        )

        Spacer(modifier = Modifier.weight(1f))

        SettingsButtonsSection(
            onManageCategoriesClick = onManageCategoriesClick,
            onManageShortcutsClick = onManageShortcutsClick
        )
    }
}

@Composable
private fun SettingsToggleSection(
    themeSelected: ThemeOption,
    onOptionSelected: (ThemeOption) -> Unit
) {
    ThemeToggle(
        config = themeToggleConfig(
            selected = themeSelected,
            onOptionSelected = onOptionSelected
        )
    )
}

@Composable
private fun SettingsSelectorSection(
    language: AppLanguage,
    currencyType: CurrencyType,
    startOfWeek: StartOfWeek,
    paymentMethod: PaymentMethodType,
    onLanguageSelectorClick: () -> Unit,
    onCurrencySelectorClick: () -> Unit,
    onStartOfWeekSelectorClick: () -> Unit,
    onPaymentMethodSelectorClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        InputSelector(
            config = InputSelectorConfig(
                onClick = onLanguageSelectorClick,
                value = stringResource(language.toLabel()),
                label = stringResource(R.string.label_language)
            )
        )

        InputSelector(
            config = InputSelectorConfig(
                onClick = onCurrencySelectorClick,
                value = getCurrencyCode(currencyType),
                label = stringResource(R.string.label_currency)
            )
        )

        InputSelector(
            config = InputSelectorConfig(
                onClick = onStartOfWeekSelectorClick,
                value = stringResource(startOfWeek.toLabel()),
                label = stringResource(R.string.label_first_day)
            )
        )

        InputSelector(
            config = InputSelectorConfig(
                onClick = onPaymentMethodSelectorClick,
                value = stringResource(paymentMethod.toLabel()),
                label = stringResource(R.string.label_payment_method)
            )
        )
    }
}

@Composable
private fun SettingsButtonsSection(
    onManageCategoriesClick: () -> Unit,
    onManageShortcutsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_manage_categories),
                type = ButtonType.SECONDARY,
                state = UiState.ENABLED,
                onClick = onManageCategoriesClick
            )
        )

        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_manage_shortcuts),
                type = ButtonType.SECONDARY,
                state = UiState.ENABLED,
                onClick = onManageShortcutsClick
            )
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSettingsScreenUI() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        SettingsScreenUI(
            uiState = SettingsUiState(
                languageSelected = AppLanguage.ENGLISH,
                currencySelected = CurrencyType.EUR,
                startOfWeekSelected = StartOfWeek.MONDAY,
                paymentMethodSelected = PaymentMethodType.CASH
            ),
            themeSelected = ThemeOption.LIGHT,
            onThemeOptionChange = {},
            onLanguageSelectorClick = {},
            onCurrencySelectorClick = {},
            onStartOfWeekSelectorClick = {},
            onPaymentMethodSelectorClick = {},
            onManageCategoriesClick = {},
            onManageShortcutsClick = {}
        )
    }
}
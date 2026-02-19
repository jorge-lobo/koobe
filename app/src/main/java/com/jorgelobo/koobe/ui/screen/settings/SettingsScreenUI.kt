package com.jorgelobo.koobe.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.screen.settings.components.SettingsButtonsSection
import com.jorgelobo.koobe.ui.screen.settings.components.SettingsSelectorSection
import com.jorgelobo.koobe.ui.screen.settings.components.SettingsToggleSection
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

/**
 * Composable that represents the UI for the Settings screen.
 *
 * This screen allows users to configure application preferences, including visual themes,
 * regional settings (language, currency, first day of the week), and manage categories or shortcuts.
 *
 * @param modifier The [Modifier] to be applied to the layout.
 * @param uiState The state containing the current selections for language, currency, start of week,
 * and payment method.
 * @param themeSelected The currently active [ThemeOption].
 * @param onThemeOptionChange Callback triggered when the theme selection changes.
 * @param onLanguageSelectorClick Callback triggered when the language selection field is clicked.
 * @param onCurrencySelectorClick Callback triggered when the currency selection field is clicked.
 * @param onStartOfWeekSelectorClick Callback triggered when the start of week selection field is clicked.
 * @param onPaymentMethodSelectorClick Callback triggered when the default payment method selection
 * field is clicked.
 * @param onManageCategoriesClick Callback triggered when the button to manage categories is clicked.
 * @param onManageShortcutsClick Callback triggered when the button to manage shortcuts is clicked.
 */
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
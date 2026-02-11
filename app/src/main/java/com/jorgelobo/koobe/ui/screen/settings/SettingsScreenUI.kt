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

/**
 * A composable that displays the theme selection toggle.
 * It allows the user to switch between different app themes (e.g., Light, Dark, System).
 *
 * @param themeSelected The currently selected [ThemeOption].
 * @param onOptionSelected A callback function that is invoked when a new theme option is selected.
 */
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

/**
 * Composable that displays a group of selection fields for application settings.
 *
 * This section includes selectors for language, currency, start of the week, and default payment
 * method, providing a consistent layout for user preferences.
 *
 * @param language The currently selected [AppLanguage].
 * @param currencyType The currently selected [CurrencyType].
 * @param startOfWeek The currently selected [StartOfWeek].
 * @param paymentMethod The currently selected [PaymentMethodType].
 * @param onLanguageSelectorClick Callback triggered when the language selector is clicked.
 * @param onCurrencySelectorClick Callback triggered when the currency selector is clicked.
 * @param onStartOfWeekSelectorClick Callback triggered when the start of week selector is clicked.
 * @param onPaymentMethodSelectorClick Callback triggered when the payment method selector is clicked.
 */
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

/**
 * A composable section that displays action buttons for managing specific app configurations.
 * Currently includes buttons for managing categories and shortcuts.
 *
 * @param onManageCategoriesClick Callback triggered when the user clicks the "Manage Categories" button.
 * @param onManageShortcutsClick Callback triggered when the user clicks the "Manage Shortcuts" button.
 */
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
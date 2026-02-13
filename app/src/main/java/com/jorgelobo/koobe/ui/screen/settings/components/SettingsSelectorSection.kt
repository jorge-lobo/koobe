package com.jorgelobo.koobe.ui.screen.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputSelector
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputSelectorConfig
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.getCurrencyCode

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
fun SettingsSelectorSection(
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
package com.jorgelobo.koobe.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.R

@Composable
fun getCurrencySymbol(currencyType: CurrencyType): String = when (currencyType) {
    CurrencyType.EUR -> stringResource(R.string.currency_euro)
    CurrencyType.USD -> stringResource(R.string.currency_dollar)
    CurrencyType.GBP -> stringResource(R.string.currency_pound)
}

@Composable
fun getCurrencyCode(currencyType: CurrencyType): String = when (currencyType) {
    CurrencyType.EUR -> stringResource(R.string.code_euro)
    CurrencyType.USD -> stringResource(R.string.code_dollar)
    CurrencyType.GBP -> stringResource(R.string.code_pound)
}
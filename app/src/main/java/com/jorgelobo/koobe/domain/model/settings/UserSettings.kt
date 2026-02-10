package com.jorgelobo.koobe.domain.model.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek

data class UserSettings(
    val language: AppLanguage,
    val currency: CurrencyType,
    val startOfWeek: StartOfWeek,
    val paymentMethod: PaymentMethodType
)
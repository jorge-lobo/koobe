package com.jorgelobo.koobe.ui.components.composed.sheets

import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.model.enums.ListType

data class ListSelectorBottomSheetConfig(
    val type: ListType,
    val selectedPaymentMethod: PaymentMethodType? = null,
    val selectedPeriod: PeriodType? = null,
    var onItemSelected: (String) -> Unit
)
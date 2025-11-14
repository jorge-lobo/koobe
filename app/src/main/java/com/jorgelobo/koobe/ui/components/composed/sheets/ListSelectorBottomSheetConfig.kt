package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.ui.components.model.enums.ListType

@Stable
data class ListSelectorBottomSheetConfig(
    val type: ListType,
    val selectedPaymentMethod: PaymentMethodType? = null,
    val selectedPeriod: PeriodType? = null,
    val selectedSortingType: SortingType? = null,
    val onItemSelected: (String) -> Unit
)
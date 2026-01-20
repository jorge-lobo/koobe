package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType

@Stable
sealed interface ListSelectorBottomSheetConfig {

    @get:StringRes
    val titleRes: Int

    data class Payment(
        val selected: PaymentMethodType,
        val onItemSelected: (PaymentMethodType) -> Unit
    ) : ListSelectorBottomSheetConfig {
        override val titleRes = R.string.bottom_sheet_headline_payment
    }

    data class Period(
        val selected: PeriodType,
        val onItemSelected: (PeriodType) -> Unit
    ) : ListSelectorBottomSheetConfig {
        override val titleRes = R.string.bottom_sheet_headline_period
    }

    data class Sorting(
        val selected: SortingType,
        val onItemSelected: (SortingType) -> Unit
    ) : ListSelectorBottomSheetConfig {
        override val titleRes = R.string.bottom_sheet_headline_sorting
    }
}
package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.bottomSheet.BaseBottomSheet
import com.jorgelobo.koobe.ui.components.base.radioButtons.PaymentMethodRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.PeriodRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.paymentMethodRadioButtonConfig
import com.jorgelobo.koobe.ui.components.base.radioButtons.periodRadioButtonConfig
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.ListType
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.BottomSheetSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ListSelectorBottomSheet(
    modifier: Modifier = Modifier,
    config: ListSelectorBottomSheetConfig,
    onDismiss: () -> Unit
) {
    val title = when (config.type) {
        ListType.PAYMENT -> stringResource(R.string.bottom_sheet_headline_payment)
        ListType.PERIOD -> stringResource(R.string.bottom_sheet_headline_period)
    }

    BaseBottomSheet(
        modifier = modifier,
        height = BottomSheetSize.Height.RadioGroup,
        title = title
    ) {
        if (config.type == ListType.PAYMENT) {
            PaymentMethodRadioButton(
                config = paymentMethodRadioButtonConfig(
                    selected = config.selectedPaymentMethod ?: PaymentMethodType.CASH,
                    onOptionSelected = { selected ->
                        config.onItemSelected(selected.name)
                        onDismiss()
                    }
                )
            )
        } else {
            PeriodRadioButton(
                config = periodRadioButtonConfig(
                    selected = config.selectedPeriod ?: PeriodType.MONTHLY,
                    onOptionSelected = { selected ->
                        config.onItemSelected(selected.name)
                        onDismiss()
                    }
                )
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewListSelectorBottomSheet() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            ListSelectorBottomSheet(
                config = ListSelectorBottomSheetConfig(
                    type = ListType.PAYMENT,
                    selectedPaymentMethod = PaymentMethodType.CARD,
                    onItemSelected = {}
                ),
                onDismiss = {}
            )

            ListSelectorBottomSheet(
                config = ListSelectorBottomSheetConfig(
                    type = ListType.PERIOD,
                    selectedPeriod = PeriodType.WEEKLY,
                    onItemSelected = {}
                ),
                onDismiss = {}
            )
        }
    }
}
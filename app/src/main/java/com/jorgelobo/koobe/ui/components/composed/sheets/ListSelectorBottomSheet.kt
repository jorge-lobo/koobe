package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.bottomSheet.AppModalBottomSheet
import com.jorgelobo.koobe.ui.components.base.bottomSheet.BaseBottomSheetContent
import com.jorgelobo.koobe.ui.components.base.radioButtons.PaymentMethodRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.PeriodRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.SortingRadioButton
import com.jorgelobo.koobe.ui.components.base.radioButtons.paymentMethodRadioButtonConfig
import com.jorgelobo.koobe.ui.components.base.radioButtons.periodRadioButtonConfig
import com.jorgelobo.koobe.ui.components.base.radioButtons.sortingRadioButtonConfig
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSelectorBottomSheet(
    sheetState: SheetState,
    config: ListSelectorBottomSheetConfig,
    onDismiss: () -> Unit
) {
    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        BaseBottomSheetContent(
            title = stringResource(config.titleRes),
            showHandle = true
        ) {
            when (config) {

                is ListSelectorBottomSheetConfig.Payment -> {
                    PaymentMethodRadioButton(
                        config = paymentMethodRadioButtonConfig(
                            selected = config.selected,
                            onOptionSelected = {
                                config.onItemSelected(it)
                                onDismiss()
                            }
                        )
                    )
                }

                is ListSelectorBottomSheetConfig.Period -> {
                    PeriodRadioButton(
                        config = periodRadioButtonConfig(
                            selected = config.selected,
                            onOptionSelected = {
                                config.onItemSelected(it)
                                onDismiss()
                            }
                        )
                    )
                }

                is ListSelectorBottomSheetConfig.Sorting -> {
                    SortingRadioButton(
                        config = sortingRadioButtonConfig(
                            selected = config.selected,
                            onOptionSelected = {
                                config.onItemSelected(it)
                                onDismiss()
                            }
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberPreviewSheetState(): SheetState =
    rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

@OptIn(ExperimentalMaterial3Api::class)
@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewListSelectorBottomSheet() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        val sheetState = rememberPreviewSheetState()

        LaunchedEffect(Unit) {
            sheetState.show()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            ListSelectorBottomSheet(
                sheetState = sheetState,
                config = ListSelectorBottomSheetConfig.Payment(
                    selected = PaymentMethodType.CARD,
                    onItemSelected = {}
                ),
                onDismiss = {}
            )

            ListSelectorBottomSheet(
                sheetState = sheetState,
                config = ListSelectorBottomSheetConfig.Sorting(
                    selected = SortingType.ALPHABETICAL,
                    onItemSelected = {}
                ),
                onDismiss = {}
            )
        }
    }
}
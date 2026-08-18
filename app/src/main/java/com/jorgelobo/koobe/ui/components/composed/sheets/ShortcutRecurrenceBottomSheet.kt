package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.bottomSheet.AppModalBottomSheet
import com.jorgelobo.koobe.ui.components.base.bottomSheet.BaseBottomSheetContent
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.dividers.AppHorizontalDivider
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence.ShortcutRecurrenceBottomSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence.ShortcutRecurrenceBottomSheetState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutRecurrenceBottomSheet(
    sheetState: SheetState,
    state: ShortcutRecurrenceBottomSheetState,
    onAction: (ShortcutRecurrenceBottomSheetAction) -> Unit
) {
    val visibleState = state as? ShortcutRecurrenceBottomSheetState.Visible ?: return

    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onAction(ShortcutRecurrenceBottomSheetAction.Dismiss) }
    ) {
        BaseBottomSheetContent(
            title = stringResource(R.string.bottom_sheet_headline_shortcut_recurrence),
            showHandle = true
        ) {
            val typography = AppTheme.typography.text
            val textColors = AppTheme.colors.textColors
            val shortcut = visibleState.shortcut
            val category = visibleState.category

            ShortcutBottomSheetHeader(
                shortcut = shortcut,
                category = category
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                val labelStyle = typography.labelLarge
                val labelColor = textColors.textPrimary
                val frequency = shortcut.period?.toRecurrenceLabel() ?: return@Row

                Text(
                    text = stringResource(R.string.label_frequency),
                    style = labelStyle,
                    color = labelColor
                )

                Text(
                    text = stringResource(frequency),
                    style = labelStyle,
                    color = labelColor
                )
            }

            AppHorizontalDivider()

            Spacer(modifier = Modifier.height(Spacing.Medium))

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_change_frequency),
                    type = ButtonType.TEXT,
                    onClick = { onAction(ShortcutRecurrenceBottomSheetAction.Change(shortcut)) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_disable_recurrence),
                    type = ButtonType.TEXT,
                    onClick = { onAction(ShortcutRecurrenceBottomSheetAction.Disable(shortcut)) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_cancel),
                    type = ButtonType.TEXT,
                    onClick = { onAction(ShortcutRecurrenceBottomSheetAction.Dismiss) }
                ),
                modifier = Modifier.fillMaxWidth()
            )
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
fun PreviewShortcutRecurrenceBottomSheet() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        val sheetState = rememberPreviewSheetState()

        LaunchedEffect(Unit) {
            sheetState.show()
        }

        val shortcut = Shortcut(
            id = 1,
            name = "Coffee",
            icon = IconPack.CAFE_SNACKS,
            categoryId = 1,
            amount = 0.80,
            currency = CurrencyType.EUR,
            paymentMethod = PaymentMethodType.CARD,
            transactionType = TransactionType.EXPENSE,
            repeat = true,
            period = PeriodType.DAILY
        )

        val category = Category(
            id = 1,
            name = "Food",
            icon = IconPack.CAFE_SNACKS,
            color = "FF00F0",
            type = TransactionType.EXPENSE
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            ShortcutRecurrenceBottomSheet(
                sheetState = sheetState,
                state = ShortcutRecurrenceBottomSheetState.Visible(
                    shortcut = shortcut,
                    category = category
                ),
                onAction = {}
            )
        }
    }
}
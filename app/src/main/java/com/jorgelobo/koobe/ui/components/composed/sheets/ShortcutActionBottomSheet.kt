package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.bottomSheet.AppModalBottomSheet
import com.jorgelobo.koobe.ui.components.base.bottomSheet.BaseBottomSheetContent
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.dividers.AppHorizontalDivider
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.mappers.toIcon
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutActionBottomSheet(
    sheetState: SheetState,
    config: ShortcutActionBottomSheetConfig,
) {
    AppModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = config.onCancel
    ) {
        BaseBottomSheetContent(
            title = stringResource(R.string.bottom_sheet_headline_shortcut_action),
            showHandle = true
        ) {
            val typography = AppTheme.typography.text
            val textColors = AppTheme.colors.textColors
            val shortcut = config.shortcut
            val category = config.category

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.Medium)
                    .height(AvatarSize.Large),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(
                    type = AvatarType.LARGE,
                    icon = shortcut.icon,
                    color = category.resolvedColor()
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = shortcut.name,
                        style = typography.titleLarge,
                        color = textColors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(Spacing.Micro))

                    Text(
                        text = category.localizedName(),
                        style = typography.bodyMedium,
                        color = textColors.textSupportMessage
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                ) {
                    val labelStyle = typography.labelLarge
                    val labelColor = textColors.textPrimary

                    Text(
                        text = stringResource(R.string.label_amount),
                        style = labelStyle,
                        color = labelColor
                    )

                    Text(
                        text = stringResource(R.string.label_payment_method),
                        style = labelStyle,
                        color = labelColor
                    )
                }

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(Spacing.MediumSmall)
                ) {
                    MoneyText(
                        amount = shortcut.amount,
                        currencyType = shortcut.currency,
                        wholeFontSize = typography.titleMedium.fontSize,
                        decimalFontSize = typography.labelMedium.fontSize,
                        textColor = textColors.textPrimary,
                        textAlign = TextAlign.Start,
                        isEnabled = true
                    )

                    Icon(
                        imageVector = shortcut.paymentMethod.toIcon().icon,
                        contentDescription = stringResource(R.string.cd_payment),
                        tint = AppTheme.colors.iconColors.iconPaymentMethod,
                        modifier = Modifier.size(IconSize.Small)
                    )
                }
            }

            AppHorizontalDivider()

            Spacer(modifier = Modifier.height(Spacing.Medium))

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_create_transaction),
                    type = ButtonType.TEXT,
                    onClick = config.onConfirm
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_edit_first),
                    type = ButtonType.TEXT,
                    onClick = config.onEdit
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_cancel),
                    type = ButtonType.TEXT,
                    onClick = config.onCancel
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
fun PreviewShortcutActionBottomSheet() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        val sheetState = rememberPreviewSheetState()

        LaunchedEffect(Unit) {
            sheetState.show()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            ShortcutActionBottomSheet(
                sheetState = sheetState,
                config = ShortcutActionBottomSheetConfig(
                    shortcut = Shortcut(
                        id = 1,
                        name = "Coffee",
                        icon = IconPack.CAFE_SNACKS,
                        categoryId = 1,
                        amount = 0.80,
                        currency = CurrencyType.EUR,
                        paymentMethod = PaymentMethodType.CARD,
                        transactionType = TransactionType.EXPENSE,
                        repeat = false
                    ),
                    category = Category(
                        id = 1,
                        name = "Food",
                        icon = IconPack.CAFE_SNACKS,
                        color = "FF00F0",
                        type = TransactionType.EXPENSE
                    ),
                    onConfirm = {},
                    onEdit = {},
                    onCancel = {}
                )
            )
        }
    }
}
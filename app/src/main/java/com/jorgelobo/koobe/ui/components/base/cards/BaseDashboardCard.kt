package com.jorgelobo.koobe.ui.components.base.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContent
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContentConfig
import com.jorgelobo.koobe.ui.components.model.card.DashboardCardData
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.DashboardCardType
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.CardSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseDashboardCard(
    modifier: Modifier = Modifier,
    type: DashboardCardType,
    isEmptyState: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val shape = AppTheme.shapes.medium

    val (label, icon, message, buttonLabel) = when (type) {
        DashboardCardType.BUDGETS -> DashboardCardData(
            label = stringResource(R.string.title_budgets),
            emptyStateIcon = IconGeneral.WALLET.icon,
            emptyStateMessage = stringResource(R.string.empty_dashboard_budgets),
            addButtonLabel = stringResource(R.string.btn_add_budget),
        )

        DashboardCardType.SHORTCUTS -> DashboardCardData(
            label = stringResource(R.string.title_user_shortcuts),
            emptyStateIcon = IconGeneral.EMPTY.icon,
            emptyStateMessage = stringResource(R.string.empty_dashboard_shortcuts),
            addButtonLabel = stringResource(R.string.btn_add_shortcut)
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Tiny)
    ) {
        Text(
            text = label,
            style = typography.bodyLarge,
            color = colors.textColors.textSecondary
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(CardSize.Dashboard.List)
                .clip(shape)
                .background(colors.containerColors.containerPrimary, shape)
                .border(BorderDimens.Base, colors.containerColors.containerOutline, shape)
                .padding(top = Spacing.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isEmptyState) {
                EmptyStateContent(
                    config = EmptyStateContentConfig(
                        message = message,
                        icon = icon,
                        iconTint = colors.iconColors.iconDisabled,
                        iconType = EmptyStateIconType.CARD
                    )
                )
            } else {
                content()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Small),
                contentAlignment = Alignment.CenterStart
            ) {
                AppButton(
                    ButtonConfig(
                        text = if (isEmptyState) buttonLabel else stringResource(R.string.btn_view_all),
                        type = ButtonType.TEXT,
                        icon = if (isEmptyState) IconGeneral.ADD else null,
                        onClick = onClick
                    )
                )
            }
        }
    }
}
package com.jorgelobo.koobe.ui.screen.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.composed.navigation.AppBottomNavigation
import com.jorgelobo.koobe.ui.components.composed.navigation.BottomNavigationDefaults
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun DashboardBottomSection(
    currentRoute: String,
    onRouteSelected: (String) -> Unit,
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Spacer(modifier = Modifier.height(Spacing.Medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            AppButton(
                ButtonConfig(
                    type = ButtonType.ADD_TRANSACTION,
                    state = UiState.ENABLED,
                    transactionType = TransactionType.INCOME,
                    onClick = onAddIncomeClick,
                    text = ""
                ),
                modifier = Modifier.weight(1f)
            )

            AppButton(
                ButtonConfig(
                    type = ButtonType.ADD_TRANSACTION,
                    state = UiState.ENABLED,
                    transactionType = TransactionType.EXPENSE,
                    onClick = onAddExpenseClick,
                    text = ""
                ),
                modifier = Modifier.weight(1f)
            )
        }

        AppBottomNavigation(
            currentRoute = currentRoute,
            items = BottomNavigationDefaults.items,
            onItemSelected = { item -> onRouteSelected(item.route) }
        )
    }
}
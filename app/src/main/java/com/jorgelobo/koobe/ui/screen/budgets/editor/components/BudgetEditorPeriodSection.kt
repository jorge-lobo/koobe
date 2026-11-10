package com.jorgelobo.koobe.ui.screen.budgets.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.base.switchButton.AppSwitchButton
import com.jorgelobo.koobe.ui.components.base.switchButton.SwitchConfig
import com.jorgelobo.koobe.ui.components.base.toggles.PeriodToggle
import com.jorgelobo.koobe.ui.components.base.toggles.periodToggleConfig
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorUiState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BudgetEditorPeriodSection(
    state: BudgetEditorUiState,
    isChecked: Boolean,
    onPeriodChange: (PeriodType) -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        horizontalAlignment = Alignment.End
    ) {
        PeriodToggle(
            config = periodToggleConfig(
                selected = state.period,
                onOptionSelected = onPeriodChange
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.btn_repeat),
                style = AppTheme.typography.text.labelLarge,
                color = AppTheme.colors.textColors.textPrimary
            )

            Spacer(modifier = Modifier.width(Spacing.Medium))

            AppSwitchButton(
                config = SwitchConfig(
                    checked = isChecked,
                    state = UiState.ENABLED,
                    onCheckedChange = onCheckedChange
                )
            )
        }
    }
}
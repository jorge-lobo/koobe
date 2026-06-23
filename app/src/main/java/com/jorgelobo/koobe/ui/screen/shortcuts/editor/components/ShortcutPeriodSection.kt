package com.jorgelobo.koobe.ui.screen.shortcuts.editor.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.components.base.inputs.fields.SelectorPeriod
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorUiState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.switchButton.AppSwitchButton
import com.jorgelobo.koobe.ui.components.base.switchButton.SwitchConfig
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ShortcutPeriodSection(
    state: ShortcutEditorUiState,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onPeriodSelectorClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isChecked) {
            SelectorPeriod(
                onClick = onPeriodSelectorClick,
                value = stringResource(
                    state.repeatFrequency?.toLabel() ?: R.string.fallback_select_repeat_period
                )
            )
        }

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
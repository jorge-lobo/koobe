package com.jorgelobo.koobe.ui.components.base.switchButton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppSwitchButton(config: SwitchConfig) {
    val switchColors = AppTheme.colors.switchButtonColors

    val primaryColor = switchColors.switchPrimary
    val secondaryColor = switchColors.switchSecondary
    val uncheckedBorerColor = switchColors.switchOutline

    val enabled = config.state == UiState.ENABLED

    Switch(
        checked = config.checked,
        onCheckedChange = { config.onCheckedChange(it) },
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = secondaryColor,
            checkedTrackColor = primaryColor,
            checkedBorderColor = primaryColor,
            uncheckedThumbColor = primaryColor,
            uncheckedTrackColor = secondaryColor,
            uncheckedBorderColor = uncheckedBorerColor
        )
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSwitchButtons() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var isChecked by remember { mutableStateOf(false) }
            AppSwitchButton(
                config = SwitchConfig(
                    checked = isChecked,
                    state = UiState.ENABLED,
                    onCheckedChange = { isChecked = it }
                )
            )
        }
    }
}
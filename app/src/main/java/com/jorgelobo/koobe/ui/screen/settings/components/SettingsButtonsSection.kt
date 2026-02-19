package com.jorgelobo.koobe.ui.screen.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

/**
 * A composable section that displays action buttons for managing specific app configurations.
 * Currently includes buttons for managing categories and shortcuts.
 *
 * @param onManageCategoriesClick Callback triggered when the user clicks the "Manage Categories" button.
 * @param onManageShortcutsClick Callback triggered when the user clicks the "Manage Shortcuts" button.
 */
@Composable
fun SettingsButtonsSection(
    onManageCategoriesClick: () -> Unit,
    onManageShortcutsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_manage_categories),
                type = ButtonType.SECONDARY,
                state = UiState.ENABLED,
                onClick = onManageCategoriesClick
            )
        )

        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_manage_shortcuts),
                type = ButtonType.SECONDARY,
                state = UiState.ENABLED,
                onClick = onManageShortcutsClick
            )
        )
    }
}
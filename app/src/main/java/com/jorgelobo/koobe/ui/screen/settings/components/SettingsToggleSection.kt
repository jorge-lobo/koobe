package com.jorgelobo.koobe.ui.screen.settings.components

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.toggles.ThemeToggle
import com.jorgelobo.koobe.ui.components.base.toggles.themeToggleConfig

/**
 * A composable that displays the theme selection toggle.
 * It allows the user to switch between different app themes (e.g., Light, Dark, System).
 *
 * @param themeSelected The currently selected [ThemeOption].
 * @param onOptionSelected A callback function that is invoked when a new theme option is selected.
 */
@Composable
fun SettingsToggleSection(
    themeSelected: ThemeOption,
    onOptionSelected: (ThemeOption) -> Unit
) {
    ThemeToggle(
        config = themeToggleConfig(
            selected = themeSelected,
            onOptionSelected = onOptionSelected
        )
    )
}
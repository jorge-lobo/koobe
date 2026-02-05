package com.jorgelobo.koobe.ui.components.base.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.UiLabel
import com.jorgelobo.koobe.domain.model.constants.enums.ReportsTabs
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.ui.theme.dimens.TabSize

@Composable
fun <T> AppTabs(
    config: TabConfig<T>,
    modifier: Modifier = Modifier
) where T : Enum<T>, T : UiLabel {
    val selected = config.selectedOption
    val colors = AppTheme.colors.tabColors

    PrimaryTabRow(
        modifier = modifier.height(TabSize.Container),
        selectedTabIndex = config.options.indexOf(selected),
        containerColor = colors.tabContainer,
        contentColor = colors.tabDisabled,
        indicator = {
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(config.options.indexOf(selected))
                    .height(TabSize.ActiveIndicator)
                    .background(
                        color = colors.tabActive,
                        shape = AppTheme.shapes.extraSmall
                    )
            )
        }
    ) {
        config.options.forEach { option ->
            Tab(
                selected = selected == option,
                onClick = { config.onSelectionChanged(option) },
                selectedContentColor = colors.tabActive,
                unselectedContentColor = colors.tabInactive,
                text = {
                    Text(
                        text = stringResource(option.toLabel()),
                        style = AppTheme.typography.text.bodySmall
                    )
                }
            )
        }
    }
}

@Composable
fun ReportsTabs() {
    var selected by remember { mutableStateOf(ReportsTabs.OVERVIEW) }

    AppTabs(
        config = TabConfig(
            options = ReportsTabs.entries,
            selectedOption = selected,
            onSelectionChanged = { selected = it }
        )
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewTabs() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            ReportsTabs()
        }
    }
}
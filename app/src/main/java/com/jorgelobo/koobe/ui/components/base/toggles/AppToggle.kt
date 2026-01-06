package com.jorgelobo.koobe.ui.components.base.toggles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jorgelobo.koobe.domain.model.constants.UiLabel
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.MetricType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.ButtonSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ToggleButtonItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors.toggleButtonColors

    Button(
        onClick = onClick,
        modifier = modifier.height(ButtonSize.ToggleButton.ButtonHeight),
        enabled = enabled,
        shape = AppTheme.shapes.smallMedium,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) colors.toggleSelectedContainer else colors.toggleUnselectedContainer,
            contentColor = if (isSelected) colors.toggleSelectedLabelText else colors.toggleUnselectedLabelText
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = AppTheme.typography.text.titleSmall,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun <T> AppToggle(
    config: ToggleConfig<T>,
    modifier: Modifier = Modifier
) where T : Enum<T>, T : UiLabel {
    val enabled = config.state == UiState.ENABLED
    val colors = AppTheme.colors.toggleButtonColors
    var selected by remember { mutableStateOf(config.selectedOption) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonSize.ToggleButton.ContainerHeight)
            .background(
                colors.toggleContainer,
                shape = AppTheme.shapes.medium
            )
    ) {
        Row(
            modifier = modifier.padding(Spacing.Micro),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Micro)
        ) {
            config.options.forEach { option ->
                ToggleButtonItem(
                    modifier = Modifier.weight(1f),
                    text = stringResource(option.toLabel()),
                    isSelected = selected == option,
                    enabled = enabled,
                    onClick = {
                        if (enabled) {
                            selected = option
                            config.onSelectionChanged(option)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionToggle(
    config: ToggleConfig<TransactionType>
) {
    AppToggle(config = config)
}

@Composable
fun PeriodToggle(
    config: ToggleConfig<PeriodType>
) {
    AppToggle(config = config)
}

@Composable
fun ThemeToggle(
    config: ToggleConfig<ThemeOption>
) {
    AppToggle(config = config)
}

@Composable
fun MetricToggle(
    config: ToggleConfig<MetricType>
) {
    AppToggle(config = config)
}

@Composable
fun CategoryDetailToggle(
    config: ToggleConfig<CategoryDetailType>
) {
    AppToggle(config = config)
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewToggleButtons() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var transactionSelected by remember { mutableStateOf(TransactionType.EXPENSE) }
            var periodSelected by remember { mutableStateOf(PeriodType.MONTHLY) }
            var themeSelected by remember { mutableStateOf(ThemeOption.LIGHT) }
            var metricSelected by remember { mutableStateOf(MetricType.BALANCE) }
            var categoryDetailSelected by remember { mutableStateOf(CategoryDetailType.SUBCATEGORIES) }

            TransactionToggle(
                config = transactionToggleConfig(
                    selected = transactionSelected,
                    onOptionSelected = { transactionSelected = it }
                )
            )

            PeriodToggle(
                config = periodToggleConfig(
                    selected = periodSelected,
                    onOptionSelected = { periodSelected = it }
                )
            )

            ThemeToggle(
                config = themeToggleConfig(
                    selected = themeSelected,
                    onOptionSelected = { themeSelected = it }
                )
            )

            MetricToggle(
                config = metricToggleConfig(
                    selected = metricSelected,
                    onOptionSelected = { metricSelected = it }
                )
            )

            CategoryDetailToggle(
                config = categoryDetailToggleConfig(
                    selected = categoryDetailSelected,
                    onOptionSelected = { categoryDetailSelected = it }
                )
            )
        }
    }
}
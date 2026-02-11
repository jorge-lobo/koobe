package com.jorgelobo.koobe.ui.components.composed.numericKeypad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.numericKeypad.BaseNumericKey
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.base.numericKeypad.keypadKeys
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.NumericKeypadSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun NumericKeypad(
    modifier: Modifier = Modifier,
    onKeyClick: (KeypadKey) -> Unit
) {
    val colors = AppTheme.colors.keypadColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                colors.keypadContainer,
                shape = AppTheme.shapes.medium
            )
            .padding(Spacing.Micro),
        verticalArrangement = Arrangement.spacedBy(Spacing.Micro)
    ) {
        keypadKeys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Micro)
            ) {
                row.forEach { key ->
                    BaseNumericKey(
                        modifier = Modifier
                            .weight(1f)
                            .height(NumericKeypadSize.KeyHeight),
                        key = key,
                        onClick = { onKeyClick(key) },
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewKeypad() {
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
            NumericKeypad(
                onKeyClick = {}
            )
        }
    }
}
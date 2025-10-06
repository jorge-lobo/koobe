package com.jorgelobo.koobe.ui.components.base.numericKeypad

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.components.model.KeyType
import com.jorgelobo.koobe.ui.components.model.KeypadKey
import com.jorgelobo.koobe.ui.components.model.keypadKeys
import com.jorgelobo.koobe.ui.icons.getIconFromName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.NumericKeypadSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseKey(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    type: KeyType,
    @StringRes labelRes: Int? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val colors = AppTheme.colors.keypadColors
    val backgroundColor = if (type == KeyType.BACKSPACE) {
        colors.keypadKeySecondaryContainer
    } else {
        if (labelRes == R.string.keypad__) colors.keypadKeySecondaryContainer
        else colors.keypadKeyPrimaryContainer
    }
    val pressedColor = colors.keypadKeyPressedContainer

    Button(
        modifier = modifier,
        shape = AppTheme.shapes.smallMedium,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) pressedColor else backgroundColor,
        ),
        interactionSource = interactionSource,
        enabled = true,
        onClick = onClick,
    ) {
        when (type) {
            KeyType.NUMERIC -> {
                labelRes?.let { res ->
                    Text(
                        text = stringResource(id = res),
                        color = colors.keypadKeySymbol,
                        style = AppTheme.typography.text.displaySmall
                    )
                }
            }

            KeyType.BACKSPACE -> {
                Icon(
                    imageVector = getIconFromName(IconGeneral.BACKSPACE),
                    contentDescription = null,
                    tint = AppTheme.colors.iconColors.iconPrimary
                )
            }
        }
    }
}

@Composable
fun NumericKeypad(
    onKeyClick: (KeypadKey) -> Unit,
    modifier: Modifier = Modifier
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
                    BaseKey(
                        modifier = Modifier
                            .weight(1f)
                            .height(NumericKeypadSize.KeyHeight),
                        onClick = { onKeyClick(key) },
                        type = key.type,
                        labelRes = key.labelRes
                    )
                }
            }
        }

    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewKeypad() {
    KoobeTheme {
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
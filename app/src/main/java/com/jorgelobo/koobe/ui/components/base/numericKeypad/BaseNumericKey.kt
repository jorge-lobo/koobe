package com.jorgelobo.koobe.ui.components.base.numericKeypad

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.enums.KeyType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.mappers.getIconFromName
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun BaseNumericKey(
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
                    contentDescription = stringResource(R.string.cd_backspace),
                    tint = AppTheme.colors.iconColors.iconPrimary
                )
            }
        }
    }
}
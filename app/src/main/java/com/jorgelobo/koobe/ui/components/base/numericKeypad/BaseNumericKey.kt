package com.jorgelobo.koobe.ui.components.base.numericKeypad

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
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun BaseNumericKey(
    modifier: Modifier = Modifier,
    key: KeypadKey,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val colors = AppTheme.colors.keypadColors

    val backgroundColor = when (key) {
        is KeypadKey.Backspace,
        is KeypadKey.Decimal -> colors.keypadKeySecondaryContainer

        is KeypadKey.Digit -> colors.keypadKeyPrimaryContainer
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
        when (key) {
            is KeypadKey.Digit ->
                Text(
                    text = key.value.toString(),
                    style = AppTheme.typography.text.displaySmall,
                    color = colors.keypadKeySymbol
                )

            KeypadKey.Decimal ->
                Text(
                    text = ".",
                    style = AppTheme.typography.text.displaySmall,
                    color = colors.keypadKeySymbol
                )

            KeypadKey.Backspace ->
                Icon(
                    imageVector = IconPack.BACKSPACE.icon,
                    contentDescription = stringResource(R.string.cd_backspace),
                    tint = AppTheme.colors.iconColors.iconPrimary
                )
        }
    }
}
package com.jorgelobo.koobe.ui.components.base.inputs.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.InputSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppInputText(
    config: InputFieldConfig,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isWarningShow: Boolean = false,
) {
    val shape = AppTheme.shapes.medium
    val colors = AppTheme.colors.textFieldColors
    val typography = AppTheme.typography.text
    val icon = IconGeneral.RESET.icon

    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val borderColor by animateColorAsState(
        targetValue = when {
            isFocused -> colors.textFieldFocusedOutline
            isWarningShow -> colors.textFieldWarningOutline
            else -> colors.textFieldDefaultOutline
        }
    )

    val placeholderColor by animateColorAsState(
        targetValue = when (config.state) {
            InputState.DEFAULT -> colors.textFieldPlaceholder
            InputState.ERROR -> colors.textFieldWarningMessage
        }
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = config.label,
            style = typography.bodySmall.copy(
                color = colors.textFieldLabel
            ),
            modifier = Modifier
                .padding(start = Spacing.Medium, bottom = Spacing.Micro)
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(InputSize.InputHeight)
                .clip(shape)
                .border(BorderDimens.Base, borderColor, shape)
                .background(colors.textFieldBackground)
                .padding(start = Spacing.Medium, end = Spacing.Small)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = config.value,
                    onValueChange = config.onValueChange,
                    enabled = enabled,
                    singleLine = true,
                    textStyle = typography.bodyLarge.copy(
                        color = colors.textFieldText
                    ),
                    cursorBrush = SolidColor(colors.textFieldCursor),
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { isFocused = it.isFocused }
                        .focusRequester(focusRequester)
                ) { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (config.value.isEmpty()) {
                            config.placeholder?.let {
                                Text(
                                    text = when (config.state) {
                                        InputState.DEFAULT -> it
                                        InputState.ERROR -> stringResource(R.string.input_warning_description)
                                    },
                                    style = when (config.state) {
                                        InputState.DEFAULT -> typography.bodySmall.copy(
                                            color = placeholderColor
                                        )

                                        InputState.ERROR -> typography.bodySmall.copy(
                                            color = placeholderColor
                                        )
                                    }
                                )
                            }
                        }
                        innerTextField()
                    }
                }

                // Trailing icon
                AnimatedVisibility(visible = config.value.isNotEmpty()) {
                    IconButton(
                        onClick = { config.onValueChange("") },
                        enabled = enabled,
                        modifier = Modifier.size(IconSize.Large)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(R.string.cd_reset_text),
                            tint = colors.textFieldIcon,
                            modifier = Modifier.size(IconSize.Medium)
                        )
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewInputText() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var text1 by remember { mutableStateOf("") }
            var text2 by remember { mutableStateOf("Description") }

            val label = stringResource(R.string.label_description)
            val placeHolder = stringResource(R.string.input_hint_description)

            AppInputText(
                config = InputFieldConfig(
                    value = text1,
                    label = label,
                    placeholder = placeHolder,
                    state = InputState.DEFAULT,
                    onValueChange = { text1 = it }
                )
            )

            AppInputText(
                config = InputFieldConfig(
                    value = text1,
                    label = label,
                    placeholder = placeHolder,
                    state = InputState.ERROR,
                    onValueChange = { text1 = it }
                )
            )

            AppInputText(
                config = InputFieldConfig(
                    value = text2,
                    label = label,
                    placeholder = placeHolder,
                    state = InputState.DEFAULT,
                    onValueChange = { text2 = it }
                )
            )
        }
    }
}
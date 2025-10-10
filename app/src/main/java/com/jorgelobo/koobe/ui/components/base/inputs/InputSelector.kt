package com.jorgelobo.koobe.ui.components.base.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.InputSelectorConfig
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.SelectorSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun InputSelector(
    config: InputSelectorConfig,
    modifier: Modifier = Modifier
) {
    AppTheme.shapes
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val icon = IconGeneral.DISCLOSURE.icon

    BaseFieldContainer(
        label = config.label,
        height = SelectorSize.InputSelectorHeight,
        modifier = modifier
    ) {
        Text(
            text = config.value,
            style = typography.bodyLarge,
            color = colors.textColors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.weight(1f))

        AnimatedIconButton(
            imageVector = icon,
            contentDescription = null,
            onClick = config.onClick
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewInputSelector() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            InputSelector(
                config = InputSelectorConfig(
                    onClick = {},
                    value = "English",
                    label = stringResource(R.string.label_language)
                )
            )
        }
    }
}
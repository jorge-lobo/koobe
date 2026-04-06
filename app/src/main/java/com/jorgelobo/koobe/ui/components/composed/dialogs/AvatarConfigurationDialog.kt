package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.dialogs.BaseDialog
import com.jorgelobo.koobe.ui.components.model.enums.AvatarConfigurationType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.DialogSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AvatarConfigurationDialog(
    modifier: Modifier = Modifier,
    config: AvatarConfigurationDialogConfig,
    enable: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val title = when (config.type) {
        AvatarConfigurationType.COLOR -> stringResource(R.string.dialog_headline_color_selector)
        AvatarConfigurationType.ICON -> stringResource(R.string.dialog_headline_icon_selector)
    }

    BaseDialog(
        modifier = modifier,
        height = DialogSize.AvatarConfigurator.DialogHeight,
        confirmText = stringResource(R.string.btn_apply),
        enable = enable,
        onDismissRequest = config.onCancel,
        onConfirm = config.onApply,
        onCancel = config.onCancel
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = typography.titleLarge,
                color = colors.textColors.textSecondary
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = DialogSize.AvatarConfigurator.ContentHeightMax)
            ) {
                content()
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewAvatarConfigurationDialog() {
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
            AvatarConfigurationDialog(
                config = AvatarConfigurationDialogConfig(
                    type = AvatarConfigurationType.COLOR,
                    onApply = {},
                    onCancel = {}
                ),
                content = {}
            )
        }
    }
}
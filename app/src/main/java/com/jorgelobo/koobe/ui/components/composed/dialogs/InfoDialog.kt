package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.dialogs.BaseDialog
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.InfoType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.DialogSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    type: InfoType,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text

    val (headline, message) = when (type) {
        InfoType.CATEGORY -> Pair(
        stringResource(R.string.dialog_headline_info_category),
        stringResource(R.string.dialog_message_info_category)
        )
        InfoType.SUBCATEGORY -> Pair(
        stringResource(R.string.dialog_headline_info_subcategory),
        stringResource(R.string.dialog_message_info_subcategory)
        )
    }

    BaseDialog(
        modifier = modifier,
        height = DialogSize.Confirmation.Discard,
        confirmText = stringResource(R.string.btn_ok),
        enable = true,
        showCancelButton = false,
        onDismissRequest = onClick,
        onConfirm = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = IconPack.INFO.icon,
                contentDescription = null,
                tint = colors.iconColors.iconWarning
            )

            Text(
                text = headline,
                style = typography.titleLarge,
                color = colors.textColors.textSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(Spacing.Medium))

        Text(
            text = message,
            style = typography.bodyLarge,
            color = colors.textColors.textPrimary
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewInfoDialog() {
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
            InfoDialog(
                type = InfoType.SUBCATEGORY,
                onClick = {}
            )
        }
    }
}
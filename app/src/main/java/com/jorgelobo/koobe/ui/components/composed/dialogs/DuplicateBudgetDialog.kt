package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jorgelobo.koobe.ui.components.base.dialogs.BaseDialog
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.DialogSize
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun DuplicateBudgetDialog(
    modifier: Modifier = Modifier,
    config: DuplicateBudgetDialogConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text

    BaseDialog(
        modifier = modifier,
        height = DialogSize.Confirmation.Delete,
        confirmText = stringResource(R.string.btn_discard),
        confirmTextColor = AccentCoral,
        dismissTest = stringResource(R.string.btn_continue_editing),
        enable = true,
        onDismissRequest = config.onContinueEditing,
        onConfirm = config.onDiscard,
        onCancel = config.onContinueEditing
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = IconPack.DISCARD.icon,
                contentDescription = null,
                tint = AccentCoral
            )

            Text(
                text = stringResource(R.string.dialog_headline_duplicate_budget),
                style = typography.titleLarge,
                color = colors.textColors.textSecondary,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(Spacing.Medium))

        Text(
            text = stringResource(R.string.dialog_message_duplicate_budget),
            style = typography.bodyLarge,
            color = colors.textColors.textPrimary
        )
    }
}
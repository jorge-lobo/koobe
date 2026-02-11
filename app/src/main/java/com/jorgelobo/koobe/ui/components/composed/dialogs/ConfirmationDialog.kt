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
import com.jorgelobo.koobe.ui.components.model.enums.ConfirmationType
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.dimens.DialogSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    config: ConfirmationDialogConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text

    val (height, icon, confirmText) = when (config.type) {
        ConfirmationType.DELETE -> Triple(
            DialogSize.Confirmation.Delete,
            IconGeneral.DELETE,
            stringResource(R.string.btn_delete)
        )

        ConfirmationType.DISCARD -> Triple(
            DialogSize.Confirmation.Discard,
            IconGeneral.DISCARD,
            stringResource(R.string.btn_discard)
        )
    }

    BaseDialog(
        modifier = modifier,
        height = height,
        confirmText = confirmText,
        confirmTextColor = AccentCoral,
        enable = true,
        onDismissRequest = config.onCancel,
        onConfirm = config.onConfirm,
        onCancel = config.onCancel
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon.icon,
                contentDescription = null,
                tint = AccentCoral
            )

            Text(
                text = config.title,
                style = typography.titleLarge,
                color = colors.textColors.textSecondary,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(Spacing.Medium))

        Text(
            text = config.message,
            style = typography.bodyLarge,
            color = colors.textColors.textPrimary
        )

        if (config.helperText != null) {
            Spacer(modifier = Modifier.height(Spacing.Medium))
            Text(
                text = config.helperText,
                style = typography.bodyLarge,
                color = colors.textColors.textPrimary
            )
        }
    }
}

@Composable
fun DiscardDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    ConfirmationDialog(
        config = ConfirmationDialogConfig(
            type = ConfirmationType.DISCARD,
            title = stringResource(R.string.dialog_headline_discard_changes),
            message = stringResource(R.string.dialog_message_discard),
            onConfirm = onConfirm,
            onCancel = onCancel
        )
    )
}

@Composable
fun DeleteDialog(
    type: DeleteType,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val (title, message, helper) = when (type) {
        DeleteType.BUDGET -> Triple(
            stringResource(R.string.dialog_headline_delete_budget),
            stringResource(R.string.dialog_message_delete_budget),
            stringResource(R.string.dialog_confirmation_delete_budget)
        )

        DeleteType.CATEGORY -> Triple(
            stringResource(R.string.dialog_headline_delete_category),
            stringResource(R.string.dialog_message_delete_category),
            stringResource(R.string.dialog_confirmation_delete_category)
        )

        DeleteType.SHORTCUT -> Triple(
            stringResource(R.string.dialog_headline_delete_shortcut),
            stringResource(R.string.dialog_message_delete_shortcut),
            stringResource(R.string.dialog_confirmation_delete_shortcut)
        )

        DeleteType.SUBCATEGORY -> Triple(
            stringResource(R.string.dialog_headline_delete_subcategory),
            stringResource(R.string.dialog_message_delete_subcategory),
            stringResource(R.string.dialog_confirmation_delete_subcategory)
        )

        DeleteType.TRANSACTION -> Triple(
            stringResource(R.string.dialog_headline_delete_transaction),
            stringResource(R.string.dialog_message_delete_transaction),
            stringResource(R.string.dialog_confirmation_delete_transaction)
        )
    }

    ConfirmationDialog(
        config = ConfirmationDialogConfig(
            type = ConfirmationType.DELETE,
            title = title,
            message = message,
            helperText = helper,
            onConfirm = onConfirm,
            onCancel = onCancel
        )
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewConfirmationDialog() {
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
            DiscardDialog(
                onConfirm = {},
                onCancel = {}
            )

            DeleteDialog(
                type = DeleteType.TRANSACTION,
                onConfirm = {},
                onCancel = {}
            )
        }
    }
}
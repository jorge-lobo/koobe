package com.jorgelobo.koobe.ui.components.base.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.composed.buttons.ConfirmCancelButtons
import com.jorgelobo.koobe.ui.components.composed.buttons.ConfirmCancelButtonsConfig
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    height: Dp,
    confirmText: String,
    enable: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = AppTheme.colors.containerColors
    val shape = AppTheme.shapes.giant
    val isPreview = LocalInspectionMode.current

    val dialogContent = @Composable {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(height),
            shape = shape,
            color = colors.containerPrimary,
            border = BorderStroke(BorderDimens.Base, colors.containerOutline),
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.MediumLarge),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                content()

                Spacer(modifier = Modifier.height(Spacing.MediumLarge))

                ConfirmCancelButtons(
                    config = ConfirmCancelButtonsConfig(
                        confirmText = confirmText,
                        cancelText = stringResource(R.string.btn_cancel),
                        isConfirmEnabled = enable,
                        onConfirmClick = onConfirm,
                        onCancelClick = onCancel
                    ),
                    modifier = Modifier.padding(end = Spacing.Medium)
                )
            }
        }
    }

    if (isPreview) {
        dialogContent()
    } else {
        Dialog(onDismissRequest = onDismissRequest) {
            dialogContent()
        }
    }
}
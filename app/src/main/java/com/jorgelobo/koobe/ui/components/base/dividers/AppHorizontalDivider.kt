package com.jorgelobo.koobe.ui.components.base.dividers

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens

@Composable
fun AppHorizontalDivider() {
    HorizontalDivider(
        thickness = BorderDimens.Base,
        color = AppTheme.colors.containerColors.divider
    )
}
package com.jorgelobo.koobe.ui.components.base.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.BottomSheetSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseBottomSheet(
    modifier: Modifier = Modifier,
    height: Dp? = null,
    title: String,
    showHandle: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = AppTheme.colors
    val shapes = AppTheme.shapes

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(if (height != null) Modifier.height(height) else Modifier.wrapContentHeight()),
        shape = shapes.giant,
        color = colors.containerColors.containerPrimary,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium)
        ) {
            if (showHandle) {
                Box(
                    modifier = Modifier
                        .width(BottomSheetSize.Handle.width)
                        .height(BottomSheetSize.Handle.height)
                        .background(colors.containerColors.bottomSheetDragHandle, shapes.extraSmall)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(Spacing.Small))
            }

            Text(
                text = title,
                style = AppTheme.typography.text.titleLarge,
                color = colors.textColors.textSecondary
            )

            Spacer(Modifier.height(Spacing.Medium))

            content()
        }
    }
}
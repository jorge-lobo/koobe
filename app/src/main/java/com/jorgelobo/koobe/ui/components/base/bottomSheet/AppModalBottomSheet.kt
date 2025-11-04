package com.jorgelobo.koobe.ui.components.base.bottomSheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jorgelobo.koobe.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    scrimColor: Color = AppTheme.colors.backgroundColors.scrim,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = AppTheme.colors
    val shape = AppTheme.shapes.giant

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = shape,
        containerColor = colors.containerColors.containerPrimary,
        dragHandle = null,
        scrimColor = scrimColor,
        tonalElevation = 4.dp,
        content = content
    )
}
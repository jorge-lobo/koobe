package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.colors.AvatarColorPalette
import com.jorgelobo.koobe.ui.components.model.enums.AvatarConfigurationType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorSelectorDialog(
    modifier: Modifier = Modifier,
    state: SelectorDialogState<Color>,
    onAction: (SelectorDialogAction<Color>) -> Unit,
    config: AvatarConfigurationDialogConfig
) {
    val shape = AppTheme.shapes.extraLarge
    val palette: List<Color> = AvatarColorPalette.colors

    val selectedColor = state.selected
    val enable = selectedColor != null

    AvatarConfigurationDialog(
        modifier = modifier,
        config = config.copy(
            type = AvatarConfigurationType.COLOR,
            onApply = { onAction(SelectorDialogAction.Apply) },
            onCancel = { onAction(SelectorDialogAction.Cancel) }
        ),
        enable = enable
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = AvatarSize.Large),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            items(items = palette) { color ->

                val isSelected = color == selectedColor

                Box(
                    modifier = Modifier
                        .size(AvatarSize.Large)
                        .clip(shape)
                        .background(color)
                        .clickable { onAction(SelectorDialogAction.Select(color)) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = IconPack.CHECK.icon,
                            contentDescription = null,
                            tint = AppTheme.colors.iconColors.iconAvatar,
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
fun PreviewColorSelectorDialog() {
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
            var showDialog by remember { mutableStateOf(true) }

            if (showDialog) {
                ColorSelectorDialog(
                    state = SelectorDialogState(
                        selected = Color.Red
                    ),
                    onAction = {},
                    config = AvatarConfigurationDialogConfig(
                        type = AvatarConfigurationType.COLOR,
                        onApply = { showDialog = false },
                        onCancel = { showDialog = false }
                    )
                )
            }
        }
    }
}
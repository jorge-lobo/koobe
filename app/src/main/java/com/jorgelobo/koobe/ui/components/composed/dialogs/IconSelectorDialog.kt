package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.mappers.iconThemesMap
import com.jorgelobo.koobe.ui.components.model.enums.AvatarConfigurationType
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.DialogSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IconSelectorDialog(
    modifier: Modifier = Modifier,
    state: SelectorDialogState<IconPack>,
    onAction: (SelectorDialogAction<IconPack>) -> Unit,
    config: AvatarConfigurationDialogConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val themes = iconThemesMap()

    val selected = state.selected
    val enable = selected != null

    AvatarConfigurationDialog(
        modifier = modifier,
        config = config.copy(
            type = AvatarConfigurationType.ICON,
            onApply = { onAction(SelectorDialogAction.Apply) },
            onCancel = { onAction(SelectorDialogAction.Cancel) }
        ),
        enable = enable
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            themes.entries.forEach { (themeName, icons) ->

                stickyHeader {
                    Text(
                        text = themeName,
                        style = typography.bodySmall,
                        color = colors.textColors.textSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Spacing.Tiny)
                    )
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(AvatarSize.Medium),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = DialogSize.AvatarConfigurator.ContentHeightMax)
                            .padding(bottom = Spacing.Medium),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Small),
                        userScrollEnabled = false
                    ) {
                        items(icons) { iconPack ->

                            val isSelected = iconPack == selected

                            Avatar(
                                type = AvatarType.MEDIUM,
                                icon = iconPack,
                                color = if (isSelected) colors.containerColors.containerSelected else colors.containerColors.containerSecondary,
                                isSelected = isSelected,
                                modifier = Modifier.clickable {
                                    onAction(SelectorDialogAction.Select(iconPack))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewIconSelectorDialog() {
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
                IconSelectorDialog(
                    config = AvatarConfigurationDialogConfig(
                        type = AvatarConfigurationType.ICON,
                        onApply = { showDialog = false },
                        onCancel = { showDialog = false }
                    ),
                    state = SelectorDialogState(
                        selected = IconPack.CREDIT_CARD
                    ),
                    onAction = {}
                )
            }
        }
    }
}
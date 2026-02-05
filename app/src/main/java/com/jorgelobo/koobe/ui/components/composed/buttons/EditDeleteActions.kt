package com.jorgelobo.koobe.ui.components.composed.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonDeleteItem
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonEditItem
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun EditDeleteActions(
    modifier: Modifier = Modifier,
    config: EditDeleteActionsConfig
) {
    Row(
        modifier = modifier.height(IconSize.Medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Tiny),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonEditItem(onClick = config.onEditClick)
        ButtonDeleteItem(onClick = config.onDeleteClick)
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewEditDeleteActions() {
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
            EditDeleteActions(
                config = EditDeleteActionsConfig(
                    onEditClick = {},
                    onDeleteClick = {}
                )
            )
        }
    }
}
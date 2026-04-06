package com.jorgelobo.koobe.ui.components.base.buttons.types

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.base.IconButtonAppBar
import com.jorgelobo.koobe.ui.components.base.buttons.base.IconButtonDisclosure
import com.jorgelobo.koobe.ui.components.base.buttons.base.IconButtonInput
import com.jorgelobo.koobe.ui.components.base.buttons.base.IconButtonListItem
import com.jorgelobo.koobe.ui.components.base.buttons.base.IconButtonNavigation
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ButtonBack(
    onClick: () -> Unit
) {
    IconButtonAppBar(
        onClick = onClick,
        icon = IconPack.BACK,
        enabled = true
    )
}

@Composable
fun ButtonClose(
    onClick: () -> Unit
) {
    IconButtonAppBar(
        onClick = onClick,
        icon = IconPack.CLOSE,
        enabled = true
    )
}

@Composable
fun ButtonDisclosure(
    onClick: () -> Unit
) {
    IconButtonDisclosure(
        onClick = onClick,
        icon = IconPack.DISCLOSURE,
        enabled = true
    )
}

@Composable
fun ButtonExpandCollapse(
    onClick: () -> Unit,
    isExpanded: Boolean
) {
    IconButtonDisclosure(
        onClick = onClick,
        icon = if (isExpanded) IconPack.COLLAPSE
        else IconPack.EXPAND,
        enabled = true
    )
}

@Composable
fun ButtonResetInput(
    onClick: () -> Unit,
    enabled: Boolean
) {
    IconButtonInput(
        onClick = onClick,
        icon = IconPack.RESET,
        enabled = enabled
    )
}

@Composable
fun ButtonEditItem(
    onClick: () -> Unit
) {
    IconButtonListItem(
        onClick = onClick,
        icon = IconPack.EDIT,
        enabled = true
    )
}

@Composable
fun ButtonDeleteItem(
    onClick: () -> Unit
) {
    IconButtonListItem(
        onClick = onClick,
        icon = IconPack.DELETE,
        enabled = true
    )
}

@Composable
fun ButtonNavLeft(
    onClick: () -> Unit,
    enabled: Boolean
) {
    IconButtonNavigation(
        onClick = onClick,
        icon = IconPack.LEFT,
        enabled = enabled
    )
}

@Composable
fun ButtonNavRight(
    onClick: () -> Unit,
    enabled: Boolean
) {
    IconButtonNavigation(
        onClick = onClick,
        icon = IconPack.RIGHT,
        enabled = enabled
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewIconButtons() {
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
            ButtonBack(onClick = {})
            ButtonClose(onClick = {})
            ButtonDisclosure(onClick = {})
            ButtonExpandCollapse(onClick = {}, isExpanded = false)
            ButtonExpandCollapse(onClick = {}, isExpanded = true)
            ButtonResetInput(onClick = {}, enabled = true)
            ButtonResetInput(onClick = {}, enabled = false)
            ButtonEditItem(onClick = {})
            ButtonDeleteItem(onClick = {})
            ButtonNavLeft(
                onClick = {},
                enabled = true
            )
            ButtonNavRight(
                onClick = {},
                enabled = true
            )
        }
    }
}
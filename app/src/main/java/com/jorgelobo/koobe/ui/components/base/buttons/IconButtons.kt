package com.jorgelobo.koobe.ui.components.base.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.icons.getIconFromName
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BackButton(
    onClick: () -> Unit
) {
    AppBarIconButton(
        onClick = onClick,
        iconUrl = getIconFromName(IconGeneral.BACK),
        enabled = true
    )
}

@Composable
fun CloseButton(
    onClick: () -> Unit
) {
    AppBarIconButton(
        onClick = onClick,
        iconUrl = getIconFromName(IconGeneral.CLOSE),
        enabled = true
    )
}

@Composable
fun DisclosureButton(
    onClick: () -> Unit
) {
    DisclosureIconButton(
        onClick = onClick,
        iconUrl = getIconFromName(IconGeneral.DISCLOSURE),
        enabled = true
    )
}

@Composable
fun ExpandCollapseButton(
    onClick: () -> Unit,
    isExpanded: Boolean
) {
    DisclosureIconButton(
        onClick = onClick,
        iconUrl = if (isExpanded) getIconFromName(IconGeneral.COLLAPSE) else getIconFromName(IconGeneral.EXPAND),
        enabled = true
    )
}

@Composable
fun ResetInputButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    InputIconButton(
        onClick = onClick,
        iconUrl = getIconFromName(IconGeneral.RESET),
        enabled = enabled
    )
}

@Composable
fun EditItemButton(
    onClick: () -> Unit
) {
    ListItemIconButton(
        onClick = onClick,
        iconUrl = getIconFromName(IconGeneral.EDIT),
        enabled = true
    )
}

@Composable
fun DeleteItemButton(
    onClick: () -> Unit
) {
    ListItemIconButton(
        onClick = onClick,
        iconUrl = getIconFromName(IconGeneral.DELETE),
        enabled = true
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewIconButtons() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            BackButton(onClick = {})
            CloseButton(onClick = {})
            DisclosureButton(onClick = {})
            ExpandCollapseButton(onClick = {}, isExpanded = false)
            ExpandCollapseButton(onClick = {}, isExpanded = true)
            ResetInputButton(onClick = {}, enabled = true)
            ResetInputButton(onClick = {}, enabled = false)
            EditItemButton(onClick = {})
            DeleteItemButton(onClick = {})
        }
    }
}
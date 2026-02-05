package com.jorgelobo.koobe.ui.components.composed.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.Transparent
import com.jorgelobo.koobe.ui.theme.dimens.BottomNavigationSize
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppBottomNavBarItem(
    modifier: Modifier = Modifier,
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val shape = AppTheme.shapes.extraLarge

    val textColor by animateColorAsState(
        targetValue = if (isSelected) colors.textColors.textActiveLabelText else colors.textColors.textUnselectedLabelText,
        label = "BottomNavTextColor"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) colors.navigationColors.navigationActiveIcon else colors.navigationColors.navigationUnselectedIcon,
        label = "BottomNavIconColor"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "BottomNavIconScale"
    )

    val indicatorColor by animateColorAsState(
        targetValue = if (isSelected) colors.navigationColors.navigationActiveIndicator
        else Transparent,
        label = "BottomNavIndicatorColor"
    )

    Column(
        modifier = modifier
            .width(BottomNavigationSize.ActiveIndicator.Width)
            .height(BottomNavigationSize.Height)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        verticalArrangement = Arrangement.spacedBy(Spacing.Tiny),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BottomNavigationSize.ActiveIndicator.Height)
                .clip(shape)
                .background(indicatorColor, shape)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(item.labelResId),
                tint = iconColor,
                modifier = Modifier
                    .size(IconSize.Medium)
                    .scale(iconScale)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = stringResource(item.labelResId),
            style = typography.bodyMedium,
            color = textColor
        )
    }
}

@Composable
fun AppBottomNavigation(
    modifier: Modifier = Modifier,
    currentRoute: String,
    items: List<BottomNavItem>,
    onItemSelected: (BottomNavItem) -> Unit
) {
    val colors = AppTheme.colors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(BottomNavigationSize.Height)
            .background(colors.containerColors.containerPrimary)
            .padding(top = Spacing.MediumSmall, bottom = Spacing.Medium),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = item.route == currentRoute
            AppBottomNavBarItem(
                item = item,
                isSelected = isSelected,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewBottomNavigation() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var currentRoute by remember { mutableStateOf("home") }

            AppBottomNavigation(
                currentRoute = currentRoute,
                items = BottomNavigationDefaults.items,
                onItemSelected = { selectedItem ->
                    currentRoute = selectedItem.route
                }
            )
        }
    }
}
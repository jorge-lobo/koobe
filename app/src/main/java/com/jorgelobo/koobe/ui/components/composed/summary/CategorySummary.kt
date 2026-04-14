package com.jorgelobo.koobe.ui.components.composed.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategorySummary(
    modifier: Modifier = Modifier,
    config: CategorySummaryConfig
) {
    val typography = AppTheme.typography.text
    val color = AppTheme.colors.textColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AvatarSize.Large),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            type = AvatarType.LARGE,
            icon = config.icon,
            color = config.color
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = config.categoryName,
                style = typography.titleLarge,
                color = color.textPrimary
            )

            config.subcategoryName?.let {
                Spacer(modifier = Modifier.height(Spacing.Micro))
                Text(
                    text = it,
                    style = typography.bodyMedium,
                    color = color.textSupportMessage
                )
            }

            if (config.subcategoryName.isNullOrEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        ChangeCategoryButton(onClick = config.onChangeClick)
    }
}

@Composable
private fun ChangeCategoryButton(onClick: () -> Unit) {
    AppButton(
        ButtonConfig(
            text = "",
            type = ButtonType.SQUARE,
            icon = IconPack.CHANGE,
            onClick = onClick
        )
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCategorySummary() {
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
            CategorySummary(
                config = CategorySummaryConfig(
                    icon = IconPack.HOME,
                    color = AccentGold,
                    categoryName = "Home",
                    subcategoryName = "Water",
                    onChangeClick = { }
                )
            )
        }
    }
}
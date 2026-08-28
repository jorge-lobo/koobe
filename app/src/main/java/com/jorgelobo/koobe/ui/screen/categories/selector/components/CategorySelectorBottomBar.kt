package com.jorgelobo.koobe.ui.screen.categories.selector.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorUiState
import com.jorgelobo.koobe.ui.screen.categories.selector.SelectorStep
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategorySelectorBottomBar(
    state: CategorySelectorUiState,
    mode: CategorySelectorMode,
    onCreateCategoryClick: () -> Unit,
    onCreateSubcategoryClick: () -> Unit,
    onCreateShortcutClick: () -> Unit,
    onProceed: () -> Unit
) {
    val textColor = AppTheme.colors.textColors
    val typography = AppTheme.typography.text

    val isCategoryStep = state.step == SelectorStep.SelectCategory
    val isSubcategoryStep = state.step == SelectorStep.SelectSubcategory
    val isSubcategorySelected = state.categoryDetailSelected == CategoryDetailType.SUBCATEGORIES

    val isEmpty = when {
        isCategoryStep -> state.categories.isEmpty()
        isSubcategorySelected -> state.subcategories.isEmpty()
        else -> state.shortcuts.isEmpty()
    }

    val createButtonTextRes = when {
        isCategoryStep -> R.string.btn_create_category
        isSubcategorySelected -> R.string.btn_create_subcategory
        else -> R.string.btn_create_shortcut
    }

    val emptyHintRes = when {
        isSubcategorySelected -> R.string.empty_hint_subcategories
        else -> R.string.empty_hint_shortcuts
    }

    val onCreateClick = when {
        isCategoryStep -> onCreateCategoryClick
        isSubcategorySelected -> onCreateSubcategoryClick
        else -> onCreateShortcutClick
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Create button
        if (isEmpty) {
            Text(
                text = stringResource(emptyHintRes),
                style = typography.bodySmall,
                color = textColor.textSupportMessage,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            AppButton(
                ButtonConfig(
                    text = stringResource(createButtonTextRes),
                    type = ButtonType.SECONDARY,
                    state = UiState.ENABLED,
                    onClick = onCreateClick
                )
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))
        }

        // Primary action
        if (mode.showActionButton || isSubcategoryStep) {
            val textRes = mode.actionButtonLabelRes ?: R.string.btn_continue

            AppButton(
                ButtonConfig(
                    text = stringResource(textRes),
                    type = ButtonType.PRIMARY,
                    state = if (state.isPrimaryActionEnabled) UiState.ENABLED else UiState.DISABLED,
                    onClick = onProceed
                )
            )
        }
    }
}
package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.common.modifiers.clearFocusOnTap
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.subcategories.components.SubcategoryInputSection
import com.jorgelobo.koobe.ui.screen.subcategories.components.SubcategorySaveButton
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

/**
 * Layout content for the Subcategory Editor screen.
 */
@Composable
fun SubcategoryEditorScreenUI(
    state: SubcategoryEditorUiState,
    config: SubcategoryEditorConfig,
    modifier: Modifier = Modifier,
    onNameChanged: (String) -> Unit,
    onIconSelectorClick: () -> Unit,
    onCategoryChangeClick: () -> Unit,
    onResetNameClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.MediumLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcategoryInputSection(
            state = state,
            config = config,
            onNameChanged = onNameChanged,
            onIconSelectorClick = onIconSelectorClick,
            onCategoryChangeClick = onCategoryChangeClick,
            onResetNameClick = onResetNameClick
        )

        Spacer(modifier = Modifier.weight(1f))

        SubcategorySaveButton(
            state = state,
            onSaveClick = onSaveClick
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSubcategoryEditorScreen() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        val isEditMode = true

        val category = Category(
            8,
            "Entertainment",
            IconPack.ENTERTAINMENT,
            "#FFD54F",
            TransactionType.EXPENSE
        )

        val subcategory = Subcategory(
            id = 5,
            categoryId = 8,
            name = "Movies",
            icon = IconPack.CINEMA
        )

        val emptySubcategory = Subcategory.empty()

        val config = SubcategoryEditorConfig(
            subcategoryId = if (isEditMode) 5 else null,
            categoryId = 8
        )

        val state = SubcategoryEditorUiState.initial(
            config = config,
            category = category,
            subcategory = if (isEditMode) subcategory else emptySubcategory
        ).copy(isSaveButtonEnabled = false)

        SubcategoryEditorScreenUI(
            state = state,
            config = state.config!!,
            onNameChanged = {},
            onIconSelectorClick = {},
            onCategoryChangeClick = {},
            onResetNameClick = {},
            onSaveClick = {}
        )
    }
}
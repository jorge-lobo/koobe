package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.common.modifiers.clearFocusOnTap
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.categories.editor.components.CategoryEditorInputSection
import com.jorgelobo.koobe.ui.screen.categories.editor.components.CategoryEditorActionButtons
import com.jorgelobo.koobe.ui.screen.categories.editor.components.CategoryEditorSubcategoriesSection
import com.jorgelobo.koobe.ui.screen.categories.editor.components.CategoryEditorToggleSection
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategoryEditorScreenUI(
    state: CategoryEditorUiState,
    config: CategoryEditorConfig,
    modifier: Modifier = Modifier,
    onNameChanged: (String) -> Unit,
    onResetNameClick: () -> Unit,
    onIconSelectorClick: () -> Unit,
    onColorSelectorClick: () -> Unit,
    onSaveClick: () -> Unit,
    onAddSubcategoryClick: () -> Unit,
    onEditSubcategory: (subcategoryId: Int) -> Unit,
    onDeleteSubcategory: (subcategoryId: Int) -> Unit,
    onTransactionTypeChange: (TransactionType) -> Unit
) {

    val subcategoryItems = remember(state.category.subcategories) {
        state.category.subcategories.map { subcategory ->
            SubcategoryItemUi(
                subcategory = subcategory
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.MediumLarge),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CategoryEditorInputSection(
            state = state,
            config = config,
            onNameChanged = onNameChanged,
            onResetNameClick = onResetNameClick,
            onIconSelectorClick = onIconSelectorClick,
            onColorSelectorClick = onColorSelectorClick
        )

        CategoryEditorToggleSection(
            transactionTypeSelected = state.transactionTypeSelected ?: TransactionType.EXPENSE,
            onTransactionTypeChange = onTransactionTypeChange
        )

        if (state.category.subcategories.isNotEmpty()) {
            CategoryEditorSubcategoriesSection(
                state = state,
                subcategories = subcategoryItems,
                onEditSubcategory = onEditSubcategory,
                onDeleteSubcategory = onDeleteSubcategory,
                onAddSubcategoryClick = onAddSubcategoryClick
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CategoryEditorActionButtons(
            state = state,
            isEmpty = state.category.subcategories.isEmpty(),
            onAddSubcategoryClick = onAddSubcategoryClick,
            onSaveClick = onSaveClick
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCategoryEditorScreen() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        val isEditMode = true

        val subcategory1 = Subcategory(
            id = 5,
            categoryId = 8,
            name = "Movies",
            icon = IconPack.CINEMA
        )

        val subcategory2 = Subcategory(
            id = 7,
            categoryId = 8,
            name = "Theatre",
            icon = IconPack.THEATRE
        )

        val category = Category(
            8,
            "Entertainment",
            IconPack.ENTERTAINMENT,
            "#FFD54F",
            TransactionType.EXPENSE,
            listOf(
                subcategory1,
                subcategory2
            )
        )

        val emptyCategory = Category.empty()

        val config = CategoryEditorConfig(
            categoryId = if (isEditMode) 8 else null
        )

        val state = CategoryEditorUiState.initial(
            config = config,
            category = if (isEditMode) category else emptyCategory
        ).copy(isSaveButtonEnabled = false)

        CategoryEditorScreenUI(
            state = state,
            config = config,
            onNameChanged = {},
            onResetNameClick = {},
            onIconSelectorClick = {},
            onColorSelectorClick = {},
            onSaveClick = {},
            onAddSubcategoryClick = {},
            onEditSubcategory = {},
            onDeleteSubcategory = {},
            onTransactionTypeChange = {}
        )
    }
}
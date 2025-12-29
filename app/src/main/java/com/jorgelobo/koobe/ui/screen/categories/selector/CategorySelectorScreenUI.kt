package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarAction
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.CommonAppBar
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.categories.selector.components.CategorySelection
import com.jorgelobo.koobe.ui.screen.categories.selector.components.SubcategorySelection
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme

@Composable
fun CategorySelectorScreenUI(
    config: CategorySelectorConfig,
    state: CategorySelectorUiState,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onTransactionTypeChange: (TransactionType) -> Unit,
    onCategorySelected: (Int) -> Unit,
    onSubcategorySelected: (Int) -> Unit,
    onShortcutSelected: (Int) -> Unit,
    onCategoryDetailSelected: (CategoryDetailType) -> Unit,
    onChangeClick: () -> Unit,
    onSubcategoryButtonClick: () -> Unit,
    onShortcutButtonClick: () -> Unit,
    onProceed: () -> Unit
) {
    val mode = config.mode

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(mode.headlineRes),
                    leadingAction = AppBarAction(mode.leadingIcon) { onBackClick() },
                    trailingActions = if (mode.showSettings) {
                        listOf(
                            AppBarAction(IconGeneral.SETTINGS) { onSettingsClick() }
                        )
                    } else emptyList()
                )
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state.step) {
                SelectorStep.SelectCategory -> CategorySelection(
                    showToggle = mode.showToggle,
                    showActionButton = mode.showActionButton,
                    actionButtonLabelRes = mode.actionButtonLabelRes,
                    categories = state.categories,
                    transactionSelected = state.transactionType,
                    onTransactionTypeChange = onTransactionTypeChange,
                    selectedCategoryId = state.selectedCategoryId,
                    onCategoryIdChange = onCategorySelected,
                    onActionButtonClick = onProceed
                )

                SelectorStep.SelectSubcategory -> SubcategorySelection(
                    category = state.categories.first { it.id == state.selectedCategoryId },
                    subcategories = state.subcategories,
                    shortcuts = state.shortcuts,
                    selectedSubcategoryId = state.selectedSubcategoryId,
                    selectedShortcutId = state.selectedShortcutId,
                    categoryDetailSelected = state.categoryDetailSelected,
                    onCategoryDetailSelected = onCategoryDetailSelected,
                    onSubcategorySelected = onSubcategorySelected,
                    onShortcutSelected = onShortcutSelected,
                    onChangeClick = onChangeClick,
                    onContinueClick = onProceed,
                    onSubcategoryButtonClick = onSubcategoryButtonClick,
                    onShortcutButtonClick = onShortcutButtonClick
                )
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCategorySelectorScreen() {
    KoobeTheme {
        val mockCategories = listOf(
            Category(1, "Home", IconPack.HOME.icon, "#FFB74D", TransactionType.EXPENSE),
            Category(2, "Grocery", IconPack.GROCERY.icon, "#DAE067", TransactionType.EXPENSE),
            Category(3, "Dining", IconPack.DINING.icon, "#FFA58A", TransactionType.EXPENSE),
            Category(
                4,
                "Transportation",
                IconPack.TRANSPORTATION.icon,
                "#3EB5A9",
                TransactionType.EXPENSE
            ),
            Category(5, "Health", IconPack.HEALTH.icon, "#BD5555", TransactionType.EXPENSE),
            Category(6, "Apparel", IconPack.APPAREL.icon, "#BA68C8", TransactionType.EXPENSE),
            Category(7, "Technology", IconPack.TECHNOLOGY.icon, "#6476D1", TransactionType.EXPENSE),
            Category(
                8,
                "Entertainment",
                IconPack.ENTERTAINMENT.icon,
                "#FFD54F",
                TransactionType.EXPENSE
            ),
            Category(9, "Education", IconPack.EDUCATION.icon, "#59BD5E", TransactionType.EXPENSE),
            Category(10, "Travel", IconPack.TRAVEL.icon, "#5A9BE0", TransactionType.EXPENSE),
            Category(
                11,
                "Essentials",
                IconPack.ESSENTIALS.icon,
                "#A1887F",
                TransactionType.EXPENSE
            ),
            Category(12, "Pets", IconPack.PETS.icon, "#F06292", TransactionType.EXPENSE),
            Category(13, "Body Care", IconPack.BODY_CARE.icon, "#E5A8F0", TransactionType.EXPENSE),
            Category(14, "Sports", IconPack.SPORTS.icon, "#B0E86F", TransactionType.EXPENSE),
            Category(15, "Family", IconPack.FAMILY.icon, "#7DC3E3", TransactionType.EXPENSE),
            Category(
                16,
                "Miscellaneous",
                IconPack.MISCELLANEOUS.icon,
                "#B0BEC5",
                TransactionType.EXPENSE
            ),
        )

        CategorySelectorScreenUI(
            config = CategorySelectorConfig(
                mode = CategorySelectorMode.CREATE_TRANSACTION,
                target = CategorySelectorTarget.TRANSACTION_EDITOR,
                initialTransactionType = TransactionType.EXPENSE,
                initialCategoryId = null,
                initialSubcategoryId = null,
                initialShortcutId = null
            ),
            state = CategorySelectorUiState(
                step = SelectorStep.SelectCategory,
                transactionType = TransactionType.EXPENSE,
                categories = mockCategories,
                subcategories = emptyList(),
                shortcuts = emptyList(),
                selectedCategoryId = null,
                selectedSubcategoryId = null,
                selectedShortcutId = null,
                categoryDetailSelected = CategoryDetailType.SUBCATEGORIES,
                isPrimaryActionEnabled = false,
                isLoading = false,
                errorMessage = null,
                initialSnapshot = InitialSnapshot(
                    transactionType = TransactionType.EXPENSE,
                    categoryId = null,
                    subcategoryId = null,
                    shortcutId = null
                ),
                showDiscardDialog = false
            ),
            onBackClick = {},
            onSettingsClick = {},
            onTransactionTypeChange = {},
            onCategorySelected = {},
            onSubcategorySelected = {},
            onShortcutSelected = {},
            onCategoryDetailSelected = {},
            onChangeClick = {},
            onSubcategoryButtonClick = {},
            onShortcutButtonClick = {},
            onProceed = {}
        )
    }
}
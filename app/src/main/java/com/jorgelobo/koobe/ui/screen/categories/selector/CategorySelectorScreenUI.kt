package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarAction
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.CommonAppBar
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.categories.selector.components.CategorySelection
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme

@Composable
fun CategorySelectorScreenUI(
    config: CategorySelectorConfig,
    categories: List<Category>,
    onBackClick: () -> Unit,
    onProceedToNext: (Int) -> Unit
) {
    val mode = config.mode

    var transactionType by remember { mutableStateOf(config.initialTransactionType) }
    var selectedCategoryId by remember { mutableStateOf(config.selectedCategoryId) }
    var stepSelected by remember { mutableStateOf(SelectorStep.SelectCategory) }

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(mode.headlineRes),
                    leadingAction = AppBarAction(mode.leadingIcon) { onBackClick() },
                    trailingActions = if (mode.showSettings) {
                        listOf(
                            AppBarAction(IconGeneral.SETTINGS) {}
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
            when (stepSelected) {
                SelectorStep.SelectCategory -> CategorySelection(
                    showToggle = mode.showToggle,
                    showActionButton = mode.showActionButton,
                    actionButtonLabelRes = mode.actionButtonLabelRes,
                    categories = categories,
                    transactionSelected = transactionType,
                    onTransactionTypeChange = { transactionType = it },
                    selectedCategoryId = selectedCategoryId,
                    onCategoryIdChange = { selectedCategoryId = it },
                    onActionButtonClick = {
                        if (selectedCategoryId != null) {
                            onProceedToNext(selectedCategoryId!!)
                        }
                    }
                )

                SelectorStep.SelectSubcategory -> {}
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
                selectedCategoryId = null,
                selectedSubcategoryId = null,
                selectedShortcutId = null
            ),
            categories = mockCategories,
            onBackClick = {},
            onProceedToNext = {}
        )
    }
}
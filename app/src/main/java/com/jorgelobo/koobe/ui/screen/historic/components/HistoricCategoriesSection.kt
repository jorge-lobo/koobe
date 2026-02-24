package com.jorgelobo.koobe.ui.screen.historic.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.ui.components.composed.cards.CardHistoricItem
import com.jorgelobo.koobe.ui.components.composed.cards.CardHistoricItemConfig
import com.jorgelobo.koobe.ui.screen.historic.CategoryHistoricUi
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun HistoricCategoriesSection(
    categories: List<CategoryHistoricUi>,
    currencyType: CurrencyType,
    onCategoryExpandToggle: (Int) -> Unit,
    onSubcategoryExpandToggle: (categoryId: Int, subcategoryId: Int) -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        items(
            items = categories,
            key = { it.category.id }
        ) { categoryUi ->

            CardHistoricItem(
                config = CardHistoricItemConfig(
                    category = categoryUi.category,
                    categoryHistory = categoryUi.history,
                    currencyType = currencyType,
                    isExpanded = categoryUi.isExpanded,
                    expandedSubcategories = categoryUi.expandedSubcategories,
                    onCategoryExpandToggle = { onCategoryExpandToggle(categoryUi.category.id) },
                    onSubcategoryExpandToggle = { subcategoryId ->
                        onSubcategoryExpandToggle(categoryUi.category.id, subcategoryId)
                    },
                    onTransactionClick = onTransactionClick
                )
            )
        }
    }
}
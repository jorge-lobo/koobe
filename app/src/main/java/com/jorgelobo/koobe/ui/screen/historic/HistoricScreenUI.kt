package com.jorgelobo.koobe.ui.screen.historic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.category.SubcategoryHistory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.historic.components.HistoricCategoriesSection
import com.jorgelobo.koobe.ui.screen.historic.components.HistoricToggleSection
import com.jorgelobo.koobe.ui.screen.historic.components.HistoricTopSection
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils

@Composable
fun HistoricScreenUI(
    state: HistoricUiState,
    modifier: Modifier = Modifier,
    onTransactionTypeChange: (TransactionType) -> Unit = {},
    onCategoryExpandToggle: (Int) -> Unit = {},
    onSubcategoryExpandToggle: (categoryId: Int, subcategoryId: Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HistoricTopSection(
            date = state.date,
            currencyType = state.currencyType,
            balance = state.balance,
            income = state.income,
            expenses = state.expenses
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        HistoricToggleSection(
            transactionTypeSelected = state.transactionTypeSelected,
            onTransactionTypeChange = onTransactionTypeChange
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        HistoricCategoriesSection(
            categories = state.categories,
            currencyType = state.currencyType,
            onCategoryExpandToggle = onCategoryExpandToggle,
            onSubcategoryExpandToggle = onSubcategoryExpandToggle
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
private fun PreviewHistoricScreenUI() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        HistoricScreenUI(
            state = previewHistoricUiState(),
            onTransactionTypeChange = {},
            onCategoryExpandToggle = {},
            onSubcategoryExpandToggle = { _, _ -> }
        )
    }
}

fun previewHistoricUiState(): HistoricUiState {

    // ------------------------------------------------------------
    // GROCERY
    // ------------------------------------------------------------

    val foodItemsSub = Subcategory(
        id = 11,
        categoryId = 2,
        name = "Food items",
        icon = IconPack.FOOD_ITEMS.icon
    )

    val drinksSub = Subcategory(
        id = 12,
        categoryId = 2,
        name = "Drinks",
        icon = IconPack.DRINKS.icon
    )

    val groceriesCategory = Category(
        id = 2,
        name = "Grocery",
        icon = IconPack.GROCERY.icon,
        color = "#DAE067",
        type = TransactionType.EXPENSE,
        subcategories = listOf(foodItemsSub, drinksSub)
    )

    val groceryTransactions = listOf(
        Transaction(
            id = 1,
            date = DateUtils.currentDate,
            description = "Lidl",
            type = TransactionType.EXPENSE,
            categoryId = 2,
            subcategoryId = 11,
            amount = 54.30,
            paymentMethod = PaymentMethodType.CARD,
            currency = CurrencyType.EUR
        ),
        Transaction(
            id = 2,
            date = DateUtils.currentDate,
            description = "Coffee shop",
            type = TransactionType.EXPENSE,
            categoryId = 2,
            subcategoryId = 12,
            amount = 12.80,
            paymentMethod = PaymentMethodType.CARD,
            currency = CurrencyType.EUR
        )
    )

    val groceriesHistory = CategoryHistory(
        category = groceriesCategory,
        transactionCount = 2,
        totalAmount = 67.10,
        subcategories = listOf(
            SubcategoryHistory(foodItemsSub, 1, 54.30, listOf(groceryTransactions[0])),
            SubcategoryHistory(drinksSub, 1, 12.80, listOf(groceryTransactions[1]))
        )
    )

    val groceriesUi = CategoryHistoricUi(
        category = groceriesCategory,
        history = groceriesHistory,
        isExpanded = true,
        expandedSubcategories = setOf(12)
    )

    // ------------------------------------------------------------
    // TRANSPORTATION
    // ------------------------------------------------------------

    val fuelSub = Subcategory(
        id = 19,
        categoryId = 4,
        name = "Fuel",
        icon = IconPack.FUEL.icon
    )

    val transitSub = Subcategory(
        id = 20,
        categoryId = 4,
        name = "Public transport",
        icon = IconPack.TRANSIT.icon
    )

    val transportCategory = Category(
        id = 4,
        name = "Transportation",
        icon = IconPack.TRANSPORTATION.icon,
        color = "#3EB5A9",
        type = TransactionType.EXPENSE,
        subcategories = listOf(fuelSub, transitSub)
    )

    val transportTransactions = listOf(
        Transaction(
            id = 3,
            date = DateUtils.currentDate,
            description = "BP Station",
            type = TransactionType.EXPENSE,
            categoryId = 4,
            subcategoryId = 19,
            amount = 70.00,
            paymentMethod = PaymentMethodType.CARD,
            currency = CurrencyType.EUR
        ),
        Transaction(
            id = 4,
            date = DateUtils.currentDate,
            description = "Metro card",
            type = TransactionType.EXPENSE,
            categoryId = 4,
            subcategoryId = 20,
            amount = 40.00,
            paymentMethod = PaymentMethodType.CARD,
            currency = CurrencyType.EUR
        )
    )

    val transportHistory = CategoryHistory(
        category = transportCategory,
        transactionCount = 2,
        totalAmount = 110.00,
        subcategories = listOf(
            SubcategoryHistory(fuelSub, 1, 70.00, listOf(transportTransactions[0])),
            SubcategoryHistory(transitSub, 1, 40.00, listOf(transportTransactions[1]))
        )
    )

    val transportUi = CategoryHistoricUi(
        category = transportCategory,
        history = transportHistory,
        isExpanded = true
    )

    // ------------------------------------------------------------
    // HOME
    // ------------------------------------------------------------

    val rentSub = Subcategory(
        id = 30,
        categoryId = 6,
        name = "Rent",
        icon = IconPack.RENT.icon
    )

    val houseMaintenanceSub = Subcategory(
        id = 31,
        categoryId = 6,
        name = "House maintenance",
        icon = IconPack.HOUSE_MAINTENANCE.icon
    )

    val homeCategory = Category(
        id = 6,
        name = "Home",
        icon = IconPack.HOME.icon,
        color = "#FFB74D",
        type = TransactionType.EXPENSE,
        subcategories = listOf(rentSub, houseMaintenanceSub)
    )

    val homeTransactions = listOf(
        Transaction(
            id = 5,
            date = DateUtils.currentDate,
            description = "Monthly rent",
            type = TransactionType.EXPENSE,
            categoryId = 6,
            subcategoryId = 30,
            amount = 950.00,
            paymentMethod = PaymentMethodType.TRANSFER,
            currency = CurrencyType.EUR
        ),
        Transaction(
            id = 6,
            date = DateUtils.currentDate,
            description = "Plumbing",
            type = TransactionType.EXPENSE,
            categoryId = 6,
            subcategoryId = 31,
            amount = 85.00,
            paymentMethod = PaymentMethodType.TRANSFER,
            currency = CurrencyType.EUR
        )
    )

    val homeHistory = CategoryHistory(
        category = homeCategory,
        transactionCount = 2,
        totalAmount = 1035.00,
        subcategories = listOf(
            SubcategoryHistory(rentSub, 1, 950.00, listOf(homeTransactions[0])),
            SubcategoryHistory(houseMaintenanceSub, 1, 85.00, listOf(homeTransactions[1]))
        )
    )

    val homeUi = CategoryHistoricUi(
        category = homeCategory,
        history = homeHistory,
        isExpanded = true,
        expandedSubcategories = setOf(31)
    )

    // ------------------------------------------------------------
    // STATE FINAL
    // ------------------------------------------------------------

    return HistoricUiState(
        currencyType = CurrencyType.EUR,
        categories = listOf(
            groceriesUi,
            transportUi,
            homeUi
        ),
        transactionTypeSelected = TransactionType.EXPENSE,
        /*onTransactionTypeChange = {},*/
        date = DateUtils.currentDate,
        balance = 2822.90,
        income = 3000.00,
        expenses = 177.10,
        /*onCategoryExpandToggle = {},
        onSubcategoryExpandToggle = { _, _ -> },*/
        isLoading = false,
        errorMessage = null
    )
}
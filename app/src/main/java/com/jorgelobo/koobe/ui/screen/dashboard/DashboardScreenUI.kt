package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.composed.appBar.LogoAppBar
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.screen.dashboard.components.DashboardBottomSection
import com.jorgelobo.koobe.ui.screen.dashboard.components.DashboardCardsSection
import com.jorgelobo.koobe.ui.screen.dashboard.components.DashboardTopSection
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils

@Composable
fun DashboardScreenUI(
    state: DashboardUiState,
    currentRoute: String,
    onRouteSelected: (String) -> Unit,
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onBudgetItemClick: (BudgetUiModel) -> Unit,
    onBudgetActionClick: () -> Unit,
    onShortcutItemClick: (ShortcutUiModel) -> Unit,
    onShortcutActionClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { LogoAppBar() },
        bottomBar = {
            DashboardBottomSection(
                currentRoute = currentRoute,
                onRouteSelected = onRouteSelected,
                onAddIncomeClick = onAddIncomeClick,
                onAddExpenseClick = onAddExpenseClick
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(Spacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DashboardTopSection(
                date = state.date,
                currencyType = state.currencyType,
                overallBalance = state.overallBalance,
                income = state.income,
                expenses = state.expenses
            )

            DashboardCardsSection(
                dailyIncome = state.dailyIncome,
                dailyExpenses = state.dailyExpenses,
                weeklyIncome = state.weeklyIncome,
                weeklyExpenses = state.weeklyExpenses,
                currencyType = state.currencyType,
                budgetItems = state.budgetItems,
                shortcutItems = state.shortcutItems,
                onBudgetItemClick = onBudgetItemClick,
                onShortcutItemClick = onShortcutItemClick,
                onBudgetActionClick = onBudgetActionClick,
                onShortcutActionClick = onShortcutActionClick
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    KoobeTheme {
        DashboardScreenUI(
            state = DashboardUiState(
                date = DateUtils.currentDate,
                currencyType = CurrencyType.EUR,
                overallBalance = 90.0,
                income = 360.0,
                expenses = 270.0,
                dailyExpenses = 20.0,
                dailyIncome = 50.0,
                weeklyExpenses = 200.0,
                weeklyIncome = 400.0,
                budgetItems = sampleBudgetUiModels(),
                shortcutItems = sampleShortcutUiModels()
            ),
            currentRoute = "home",
            onRouteSelected = { "home" },
            onAddIncomeClick = {},
            onAddExpenseClick = {},
            onBudgetItemClick = {},
            onBudgetActionClick = {},
            onShortcutItemClick = {},
            onShortcutActionClick = {}
        )
    }
}

private fun sampleBudgetUiModels(): List<BudgetUiModel> {
    val categoryFood = Category(
        id = 1,
        name = "Dining",
        icon = IconPack.DINING.icon,
        color = "#FFB74D",
        type = TransactionType.EXPENSE
    )

    val subcategoryDining = Subcategory(
        id = 11,
        categoryId = 1,
        name = "Restaurant",
        icon = IconPack.RESTAURANT.icon
    )

    val budget1 = Budget(
        id = 101,
        categoryId = 1,
        subcategoryId = 11,
        period = PeriodType.MONTHLY,
        repeat = true,
        paymentMethod = null,
        currency = CurrencyType.EUR,
        limitAmount = 200.0,
        spentAmount = 150.0,
        projectedAmount = 190.0,
        dailyAverage = 7.2
    )

    val budget2 = budget1.copy(
        id = 102,
        subcategoryId = 12,
        limitAmount = 300.0,
        spentAmount = 280.0,
        projectedAmount = 310.0
    )

    val budget3 = budget1.copy(
        id = 103,
        subcategoryId = 13,
        limitAmount = 100.0,
        spentAmount = 95.0,
        projectedAmount = 98.0
    )

    return listOf(
        BudgetUiModel(budget1, categoryFood, subcategoryDining),
        BudgetUiModel(budget2, categoryFood, subcategoryDining),
        BudgetUiModel(budget3, categoryFood, subcategoryDining)
    )
}

private fun sampleShortcutUiModels(): List<ShortcutUiModel> {
    val shortcutElectricity = Shortcut(
        id = 1,
        name = "Electricity",
        icon = IconPack.ELECTRICITY.icon,
        categoryId = 1,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 30.0,
        repeat = false,
    )

    val shortcutInternetTv = Shortcut(
        id = 2,
        name = "Internet & TV",
        icon = IconPack.INTERNET_TV.icon,
        categoryId = 1,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 30.0,
        repeat = false,
    )

    val shortcutRestaurant = Shortcut(
        id = 3,
        name = "Restaurant",
        icon = IconPack.RESTAURANT.icon,
        categoryId = 2,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 30.0,
        repeat = false,
    )

    val shortcutCanteen = Shortcut(
        id = 4,
        name = "Canteen",
        icon = IconPack.CANTEEN.icon,
        categoryId = 2,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 30.0,
        repeat = false,
    )

    val categoryHome = Category(
        id = 1,
        name = "Home",
        icon = IconPack.HOME.icon,
        color = "#FF5722",
        type = TransactionType.EXPENSE
    )

    val categoryDining = Category(
        id = 2,
        name = "Dining",
        icon = IconPack.DINING.icon,
        color = "#FF5722",
        type = TransactionType.EXPENSE
    )

    return listOf(
        ShortcutUiModel(shortcutElectricity, categoryHome),
        ShortcutUiModel(shortcutInternetTv, categoryHome),
        ShortcutUiModel(shortcutRestaurant, categoryDining),
        ShortcutUiModel(shortcutCanteen, categoryDining)
    )
}
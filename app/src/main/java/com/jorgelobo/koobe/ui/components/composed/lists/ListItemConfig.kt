package com.jorgelobo.koobe.ui.components.composed.lists

sealed class ListItemConfig {
    data class CategoryInfoRow(
        val avatar: String,
        val name: String,
        val numberTransactions: Int,
        val amount: Double,
        val isExpanded: Boolean,
        val onDropdownClick: () -> Unit
    ) : ListItemConfig()

    data class SubcategoryInfoRow(
        val avatar: String,
        val name: String,
        val numberTransactions: Int,
        val amount: Double,
        val isExpanded: Boolean,
        val onDropdownClick: () -> Unit
    ) : ListItemConfig()

    data class TransactionRow(
        val date: String,
        val name: String,
        val amount: Double,
        val paymentMethodIcon: String,
        val onClick: () -> Unit
    ) : ListItemConfig()

    data class PeriodInfoRow(
        val name: String,
        val numberBudgets: Int,
        val totalPercentage: Float,
        val totalBalance: Double,
        val isExpanded: Boolean,
        val onDropdownClick: () -> Unit
    ) : ListItemConfig()

    data class BudgetRow(
        val avatar: String,
        val name: String,
        val balance: Double,
        val dailyAverage: Double,
        val budget: Double,
        val spent: Double,
        val projection: Double,
        val projectionIcon: String,
        val progress: Float,
        val percentage: Float,
        val onClick: () -> Unit
    ) : ListItemConfig()
}
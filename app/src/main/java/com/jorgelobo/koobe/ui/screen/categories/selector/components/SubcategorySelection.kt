package com.jorgelobo.koobe.ui.screen.categories.selector.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonText
import com.jorgelobo.koobe.ui.components.base.toggles.CategoryDetailToggle
import com.jorgelobo.koobe.ui.components.base.toggles.categoryDetailToggleConfig
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContent
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContentConfig
import com.jorgelobo.koobe.ui.components.composed.grids.ShortcutsGrid
import com.jorgelobo.koobe.ui.components.composed.grids.ShortcutsGridConfig
import com.jorgelobo.koobe.ui.components.composed.grids.SubcategoriesGrid
import com.jorgelobo.koobe.ui.components.composed.grids.SubcategoriesGridConfig
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummary
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummaryConfig
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.components.model.subcategory.SubcategoryUiModel
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey2
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun SubcategorySelection(
    category: Category,
    subcategories: List<Subcategory>,
    shortcuts: List<Shortcut>,
    selectedSubcategoryId: Int?,
    selectedShortcutId: Int?,
    categoryDetailSelected: CategoryDetailType,
    onCategoryDetailSelected: (CategoryDetailType) -> Unit,
    onSubcategorySelected: (Int) -> Unit,
    onShortcutSelected: (Int) -> Unit,
    onChangeClick: () -> Unit,
    onContinueClick: () -> Unit,
    onCreateSubcategoryClick: () -> Unit,
    onCreateShortcutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textColor = AppTheme.colors.textColors
        val typography = AppTheme.typography.text

        val canContinue = when (categoryDetailSelected) {
            CategoryDetailType.SUBCATEGORIES -> selectedSubcategoryId != null
            CategoryDetailType.SHORTCUTS -> selectedShortcutId != null
        }

        val selectedSubcategory = subcategories.firstOrNull { it.id == selectedSubcategoryId }

        CategorySummary(
            config = CategorySummaryConfig(
                icon = category.icon,
                color = category.resolvedColor(),
                categoryName = category.localizedName(),
                subcategoryName = selectedSubcategory?.localizedName(),
                onChangeClick = onChangeClick
            )
        )

        CategoryDetailToggle(
            config = categoryDetailToggleConfig(
                selected = categoryDetailSelected,
                onOptionSelected = onCategoryDetailSelected
            )
        )

        when (categoryDetailSelected) {
            CategoryDetailType.SUBCATEGORIES -> {
                if (subcategories.isEmpty()) {
                    Spacer(modifier = Modifier.weight(1f))

                    EmptyStateContent(
                        config = EmptyStateContentConfig(
                            message = stringResource(R.string.empty_headline_subcategories),
                            icon = IconGeneral.EMPTY.icon,
                            iconTint = LightThemeGrey2,
                            iconType = EmptyStateIconType.BACKGROUND
                        )
                    )
                } else {
                    SubcategoriesGrid(
                        config = SubcategoriesGridConfig(
                            list = subcategories.map { SubcategoryUiModel(it, category) },
                            selectedSubcategoryId = selectedSubcategoryId,
                            onSubcategoryClick = onSubcategorySelected
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ButtonText(
                            onClick = onCreateSubcategoryClick,
                            enabled = true,
                            text = stringResource(R.string.btn_create_subcategory),
                            textColor = AppTheme.colors.buttonColors.buttonTextDefault,
                            iconUrl = IconGeneral.ADD.icon
                        )
                    }
                }
            }

            CategoryDetailType.SHORTCUTS -> {
                if (shortcuts.isEmpty()) {
                    Spacer(modifier = Modifier.weight(1f))

                    EmptyStateContent(
                        config = EmptyStateContentConfig(
                            message = stringResource(R.string.empty_headline_shortcuts),
                            icon = IconGeneral.EMPTY.icon,
                            iconTint = LightThemeGrey2,
                            iconType = EmptyStateIconType.BACKGROUND
                        )
                    )
                } else {
                    ShortcutsGrid(
                        config = ShortcutsGridConfig(
                            list = shortcuts.map { ShortcutUiModel(it, category) },
                            selectedShortcutId = selectedShortcutId,
                            onShortcutClick = onShortcutSelected
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ButtonText(
                            onClick = onCreateShortcutClick,
                            enabled = true,
                            text = stringResource(R.string.btn_create_shortcut),
                            textColor = AppTheme.colors.buttonColors.buttonTextDefault,
                            iconUrl = IconGeneral.ADD.icon
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        when (categoryDetailSelected) {
            CategoryDetailType.SUBCATEGORIES -> {
                if (subcategories.isEmpty()) {
                    Text(
                        text = stringResource(R.string.empty_hint_subcategories),
                        style = typography.bodySmall,
                        color = textColor.textSupportMessage,
                        textAlign = TextAlign.Center
                    )

                    AppButton(
                        ButtonConfig(
                            text = stringResource(R.string.btn_create_subcategory),
                            type = ButtonType.SECONDARY,
                            state = UiState.ENABLED,
                            onClick = onCreateSubcategoryClick
                        )
                    )
                }
            }

            CategoryDetailType.SHORTCUTS -> {
                if (shortcuts.isEmpty()) {
                    Text(
                        text = stringResource(R.string.empty_hint_shortcuts),
                        style = typography.bodySmall,
                        color = textColor.textSupportMessage,
                        textAlign = TextAlign.Center
                    )

                    AppButton(
                        ButtonConfig(
                            text = stringResource(R.string.btn_create_shortcut),
                            type = ButtonType.SECONDARY,
                            state = UiState.ENABLED,
                            onClick = onCreateShortcutClick
                        )
                    )
                }
            }
        }

        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_continue),
                type = ButtonType.PRIMARY,
                state = if (canContinue) UiState.ENABLED else UiState.DISABLED,
                onClick = onContinueClick
            )
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSubcategorySelection() {
    KoobeTheme {
        var categoryDetailSelected by remember { mutableStateOf(CategoryDetailType.SUBCATEGORIES) }
        var selectedSubcategoryId by remember { mutableStateOf<Int?>(null) }
        var selectedShortcutId by remember { mutableStateOf<Int?>(null) }

        val mockShortcuts = listOf(
            Shortcut(
                id = 1,
                name = "Electricity",
                icon = IconPack.ELECTRICITY.icon,
                categoryId = 1,
                transactionType = TransactionType.EXPENSE,
                paymentMethod = PaymentMethodType.CASH,
                currency = CurrencyType.EUR,
                amount = 30.0,
                repeat = false,
            ),
            Shortcut(
                id = 2,
                name = "Internet & TV",
                icon = IconPack.INTERNET_TV.icon,
                categoryId = 1,
                transactionType = TransactionType.EXPENSE,
                paymentMethod = PaymentMethodType.CASH,
                currency = CurrencyType.EUR,
                amount = 30.0,
                repeat = false,
            ),
            Shortcut(
                id = 3,
                name = "Restaurant",
                icon = IconPack.RESTAURANT.icon,
                categoryId = 2,
                transactionType = TransactionType.EXPENSE,
                paymentMethod = PaymentMethodType.CASH,
                currency = CurrencyType.EUR,
                amount = 30.0,
                repeat = false,
            ),
            Shortcut(
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
        )

        val mockSubcategories = listOf(
            Subcategory(
                id = 1,
                categoryId = 1,
                name = "Rent",
                icon = IconPack.RENT.icon
            ),
            Subcategory(
                id = 2,
                categoryId = 1,
                name = "Internet & TV",
                icon = IconPack.INTERNET_TV.icon
            ),
            Subcategory(
                id = 3,
                categoryId = 1,
                name = "Electricity",
                icon = IconPack.ELECTRICITY.icon
            ),
            Subcategory(
                id = 4,
                categoryId = 1,
                name = "Appliances",
                icon = IconPack.APPLIANCES.icon
            ),
            Subcategory(
                id = 5,
                categoryId = 1,
                name = "Water",
                icon = IconPack.WATER.icon
            ),
            Subcategory(
                id = 6,
                categoryId = 1,
                name = "Gas",
                icon = IconPack.GAS.icon
            ),
            Subcategory(
                id = 7,
                categoryId = 1,
                name = "Maintenance",
                icon = IconPack.HOUSE_MAINTENANCE.icon
            ),
            Subcategory(
                id = 8,
                categoryId = 1,
                name = "Condominium",
                icon = IconPack.CONDOMINIUM.icon
            ),
            Subcategory(
                id = 9,
                categoryId = 1,
                name = "Mortgage",
                icon = IconPack.MORTGAGE.icon
            ),
            Subcategory(
                id = 10,
                categoryId = 1,
                name = "Furniture",
                icon = IconPack.FURNITURE.icon
            )
        )

        val category =
            Category(1, "Home", IconPack.HOME.icon, "#FFB74D", TransactionType.EXPENSE)

        SubcategorySelection(
            category = category,
            subcategories = mockSubcategories,
            shortcuts = mockShortcuts,
            selectedSubcategoryId = selectedSubcategoryId,
            selectedShortcutId = selectedShortcutId,
            categoryDetailSelected = categoryDetailSelected,
            onCategoryDetailSelected = { categoryDetailSelected = it },
            onSubcategorySelected = { selectedSubcategoryId = it },
            onShortcutSelected = { selectedShortcutId = it },
            onChangeClick = {},
            onContinueClick = {},
            onCreateSubcategoryClick = {},
            onCreateShortcutClick = {}
        )
    }
}
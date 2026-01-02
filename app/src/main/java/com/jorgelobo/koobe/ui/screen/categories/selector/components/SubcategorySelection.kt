package com.jorgelobo.koobe.ui.screen.categories.selector.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.toggles.CategoryDetailToggle
import com.jorgelobo.koobe.ui.components.base.toggles.categoryDetailToggleConfig
import com.jorgelobo.koobe.ui.components.composed.grids.ShortcutsGrid
import com.jorgelobo.koobe.ui.components.composed.grids.ShortcutsGridConfig
import com.jorgelobo.koobe.ui.components.composed.grids.SubcategoriesGrid
import com.jorgelobo.koobe.ui.components.composed.grids.SubcategoriesGridConfig
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummary
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummaryConfig
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.components.model.subcategory.SubcategoryUiModel
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.theme.KoobeTheme
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
        val isSubcategory = categoryDetailSelected == CategoryDetailType.SUBCATEGORIES
        val selectedSubcategory = subcategories.firstOrNull { it.id == selectedSubcategoryId }

        val detailUi = if (isSubcategory) {
            CategoryDetailUi(
                isEmpty = subcategories.isEmpty(),
                canContinue = selectedSubcategoryId != null,
                emptyHeadline = R.string.empty_headline_subcategories,
                emptyHint = R.string.empty_hint_subcategories,
                createButton = R.string.btn_create_subcategory,
                onCreate = onCreateSubcategoryClick
            )
        } else {
            CategoryDetailUi(
                isEmpty = shortcuts.isEmpty(),
                canContinue = selectedShortcutId != null,
                emptyHeadline = R.string.empty_headline_shortcuts,
                emptyHint = R.string.empty_hint_shortcuts,
                createButton = R.string.btn_create_shortcut,
                onCreate = onCreateShortcutClick
            )
        }

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

        CategoryDetailContent(
            isEmpty = detailUi.isEmpty,
            isContinueEnabled = detailUi.canContinue,
            emptyHeadlineRes = detailUi.emptyHeadline,
            emptyHintRes = detailUi.emptyHint,
            createButtonRes = detailUi.createButton,
            onCreateClick = detailUi.onCreate,
            onContinueClick = onContinueClick
        ) {
            if (isSubcategory) {
                SubcategoriesGrid(
                    config = SubcategoriesGridConfig(
                        list = subcategories.map { SubcategoryUiModel(it, category) },
                        selectedSubcategoryId = selectedSubcategoryId,
                        onSubcategoryClick = onSubcategorySelected
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
            }
        }
    }
}

data class CategoryDetailUi(
    val isEmpty: Boolean,
    val canContinue: Boolean,
    val emptyHeadline: Int,
    val emptyHint: Int,
    val createButton: Int,
    val onCreate: () -> Unit
)

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
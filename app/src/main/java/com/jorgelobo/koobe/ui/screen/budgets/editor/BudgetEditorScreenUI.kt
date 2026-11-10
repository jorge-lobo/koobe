package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.common.modifiers.clearFocusOnTap
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.budgets.editor.components.BudgetEditorAmountSection
import com.jorgelobo.koobe.ui.screen.budgets.editor.components.BudgetEditorCategorySection
import com.jorgelobo.koobe.ui.screen.budgets.editor.components.BudgetEditorPeriodSection
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BudgetEditorScreenUI(
    state: BudgetEditorUiState,
    modifier: Modifier = Modifier,
    isRepeatChecked: Boolean,
    onChangeCategoryClick: () -> Unit,
    onChangePeriodClick: (PeriodType) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onResetAmountClick: () -> Unit,
    onKeyClick: (KeypadKey) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .padding(horizontal = Spacing.Medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Spacing.Large))

        BudgetEditorCategorySection(
            state = state,
            onChangeClick = onChangeCategoryClick
        )

        Spacer(modifier = Modifier.height(Spacing.Large))

        BudgetEditorPeriodSection(
            state = state,
            isChecked = isRepeatChecked,
            onPeriodChange = onChangePeriodClick,
            onCheckedChange = onCheckedChange
        )

        Spacer(modifier = Modifier.weight(1f))

        BudgetEditorAmountSection(
            state = state,
            onResetAmountClick = onResetAmountClick,
            onKeyClick = onKeyClick
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewBudgetEditorScreen() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        val category = Category(
            8,
            "Entertainment",
            IconPack.ENTERTAINMENT,
            "#FFD54F",
            TransactionType.EXPENSE
        )

        BudgetEditorScreenUI(
            state = BudgetEditorUiState(
                budget = Budget.empty(),
                category = category,
                subcategory = category.subcategories.first(),
                inputState = InputState.DEFAULT,
                budgetInitialSnapshot = BudgetInitialSnapshot(
                    categoryId = category.id,
                    subcategoryId = category.subcategories.first().id,
                    isRepeat = false,
                    period = PeriodType.MONTHLY,
                    limitAmount = 0.0
                ),
                discardDialog = ConfirmationDialogState(visible = false),
                deleteDialog = ConfirmationDialogState(visible = false)
            ),
            isRepeatChecked = true,
            onChangeCategoryClick = {},
            onChangePeriodClick = {},
            onCheckedChange = {},
            onResetAmountClick = {},
            onKeyClick = {}
        )
    }
}
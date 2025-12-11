package com.jorgelobo.koobe.ui.screen.categories.selector.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.toggles.TransactionToggle
import com.jorgelobo.koobe.ui.components.base.toggles.transactionToggleConfig
import com.jorgelobo.koobe.ui.components.composed.grids.CategoriesGrid
import com.jorgelobo.koobe.ui.components.composed.grids.CategoriesGridConfig
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategorySelection(
    showToggle: Boolean,
    showActionButton: Boolean,
    actionButtonLabelRes: Int? = null,
    categories: List<Category>,
    transactionSelected: TransactionType,
    onTransactionTypeChange: (TransactionType) -> Unit,
    selectedCategoryId: Int?,
    onCategoryIdChange: (Int?) -> Unit,
    onActionButtonClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.MediumLarge),
        verticalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showToggle) {
            TransactionToggle(
                config = transactionToggleConfig(
                    selected = transactionSelected,
                    onOptionSelected = onTransactionTypeChange
                )
            )
        }

        CategoriesGrid(
            config = CategoriesGridConfig(
                list = categories,
                selectedCategoryId = selectedCategoryId,
                onCategoryClick = onCategoryIdChange
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        if (showActionButton) {
            val textRes = actionButtonLabelRes ?: R.string.btn_continue

            AppButton(
                ButtonConfig(
                    text = stringResource(textRes),
                    type = ButtonType.PRIMARY,
                    state = UiState.ENABLED,
                    onClick = onActionButtonClick
                )
            )
        }
    }
}
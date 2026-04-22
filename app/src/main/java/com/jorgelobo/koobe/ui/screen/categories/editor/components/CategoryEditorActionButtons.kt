package com.jorgelobo.koobe.ui.screen.categories.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorUiState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategoryEditorActionButtons(
    state: CategoryEditorUiState,
    isEmpty: Boolean,
    onAddSubcategoryClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val hintRes = when (state.transactionTypeSelected) {
        TransactionType.EXPENSE -> R.string.empty_hint_subcategories_expenses
        TransactionType.INCOME -> R.string.empty_hint_subcategories_income
        null -> R.string.empty_hint_subcategories_expenses
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        if (isEmpty) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = stringResource(hintRes),
                    style = AppTheme.typography.text.bodySmall,
                    color = AppTheme.colors.textColors.textSupportMessage,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spacing.Medium))

                AppButton(
                    ButtonConfig(
                        text = stringResource(R.string.btn_add_subcategory),
                        type = ButtonType.SECONDARY,
                        state = UiState.ENABLED,
                        onClick = onAddSubcategoryClick
                    )
                )
            }
        }

        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_save),
                type = ButtonType.PRIMARY,
                state = if (state.isSaveButtonEnabled) UiState.ENABLED else UiState.DISABLED,
                onClick = onSaveClick
            )
        )
    }
}
package com.jorgelobo.koobe.ui.screen.transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputDate
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputDateConfig
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorUiState
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.date.DateUtils

/**
 * Displays transaction date controls: "Today" button and date picker input.
 */
@Composable
fun TransactionDateSection(
    state: TransactionEditorUiState,
    onTodayClick: () -> Unit,
    onDatePickClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_today),
                type = ButtonType.SECONDARY_COMPACT,
                state = if (DateUtils.isSameDay(
                        state.date,
                        DateUtils.currentDate
                    )
                ) UiState.DISABLED else UiState.ENABLED,
                onClick = onTodayClick
            )
        )

        InputDate(
            config = InputDateConfig(
                date = state.date,
                onIconClick = onDatePickClick
            )
        )
    }
}
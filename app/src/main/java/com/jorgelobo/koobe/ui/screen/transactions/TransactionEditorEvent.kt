package com.jorgelobo.koobe.ui.screen.transactions

import androidx.annotation.StringRes
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral

sealed interface TransactionEditorEvent {
    object ExitToOrigin : TransactionEditorEvent

    data class ShowSnackBar(
        @field:StringRes val messageRes: Int,
        @field:StringRes val actionLabelRes: Int? = null,
        val icon: IconGeneral? = null
    ) : TransactionEditorEvent
}
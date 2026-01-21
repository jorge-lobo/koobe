package com.jorgelobo.koobe.ui.screen.transactions

sealed interface TransactionEditorEvent {
    object ExitToOrigin : TransactionEditorEvent
}
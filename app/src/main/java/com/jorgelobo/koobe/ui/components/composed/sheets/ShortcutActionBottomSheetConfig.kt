package com.jorgelobo.koobe.ui.components.composed.sheets

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

data class ShortcutActionBottomSheetConfig(
    val shortcut: Shortcut,
    val category: Category,
    val onConfirm: () -> Unit,
    val onEdit: () -> Unit,
    val onCancel: () -> Unit
)
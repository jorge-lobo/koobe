package com.jorgelobo.koobe.ui.screen.subcategories

import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

data class SubcategoryInputState(
    val name: String? = null,
    val icon: IconPack? = null,
    val categoryId: Int? = null,
    val discardDialog: ConfirmationDialogState? = null,
    val deleteDialog: ConfirmationDialogState? = null,
    val iconSelectorDialog: SelectorDialogState<IconPack>? = null
)
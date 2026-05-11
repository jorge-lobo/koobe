package com.jorgelobo.koobe.ui.screen.categories.editor.state

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorDeleteTarget
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

// Ephemeral UI state not derived from the repository — dialogs, loading flags, and validation state.
// Never exposed directly to the screen.
data class CategoryUiStateInternal(
    val deleteTarget: CategoryEditorDeleteTarget = CategoryEditorDeleteTarget.None,
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val infoDialog: InfoDialogState = InfoDialogState(),
    val iconSelectorDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val colorSelectorDialog: SelectorDialogState<Color> = SelectorDialogState(),
    val isDeleting: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: NameValidationException? = null,
    val hasTriedToSave: Boolean = false
)
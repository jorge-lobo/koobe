package com.jorgelobo.koobe.ui.screen.categories.editor

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryFormState
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryUiStateInternal
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

object CategoryEditorReducer {

    data class Result(
        val form: CategoryFormState,
        val internal: CategoryUiStateInternal
    )

    fun reduce(
        intent: CategoryEditorIntent.State,
        currentForm: CategoryFormState,
        currentInternal: CategoryUiStateInternal
    ): Result {

        return when (intent) {

            is CategoryEditorIntent.State.NameChanged -> {
                Result(
                    form = currentForm.copy(name = FieldUpdate.Updated(intent.name)),
                    internal = currentInternal
                )
            }

            is CategoryEditorIntent.State.IconSelected -> {
                Result(
                    form = currentForm.copy(icon = FieldUpdate.Updated(intent.icon)),
                    internal = currentInternal
                )
            }

            is CategoryEditorIntent.State.ColorSelected -> {
                Result(
                    form = currentForm.copy(color = FieldUpdate.Updated(intent.color)),
                    internal = currentInternal
                )
            }

            is CategoryEditorIntent.State.TypeSelected -> {
                Result(
                    form = currentForm.copy(type = FieldUpdate.Updated(intent.type)),
                    internal = currentInternal
                )
            }

            is CategoryEditorIntent.State.ShowDiscardDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(discardDialog = ConfirmationDialogState(visible = true))
                )
            }

            is CategoryEditorIntent.State.HideDiscardDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(discardDialog = ConfirmationDialogState(visible = false))
                )
            }

            is CategoryEditorIntent.State.ShowDeleteDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(deleteDialog = ConfirmationDialogState(visible = true))
                )
            }

            is CategoryEditorIntent.State.HideDeleteDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(deleteDialog = ConfirmationDialogState(visible = false))
                )
            }

            is CategoryEditorIntent.State.ShowInfoDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(infoDialog = InfoDialogState(visible = true))
                )
            }

            is CategoryEditorIntent.State.HideInfoDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(infoDialog = InfoDialogState(visible = false))
                )
            }

            is CategoryEditorIntent.State.ShowColorSelectorDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(colorSelectorDialog = SelectorDialogState(visible = true))
                )
            }

            is CategoryEditorIntent.State.HideColorSelectorDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(colorSelectorDialog = SelectorDialogState(visible = false))
                )
            }

            is CategoryEditorIntent.State.ShowIconSelectorDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(iconSelectorDialog = SelectorDialogState(visible = true))
                )
            }

            is CategoryEditorIntent.State.HideIconSelectorDialog -> {
                Result(
                    form = currentForm,
                    internal = currentInternal.copy(iconSelectorDialog = SelectorDialogState(visible = false))
                )
            }
        }
    }
}
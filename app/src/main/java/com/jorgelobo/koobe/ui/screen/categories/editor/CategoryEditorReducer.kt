package com.jorgelobo.koobe.ui.screen.categories.editor

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryFormState
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryUiStateInternal

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

            is CategoryEditorIntent.State.SubcategoriesChanged -> {
                Result(
                    form = currentForm.copy(subcategories = FieldUpdate.Updated(intent.subcategories)),
                    internal = currentInternal
                )
            }
        }
    }
}
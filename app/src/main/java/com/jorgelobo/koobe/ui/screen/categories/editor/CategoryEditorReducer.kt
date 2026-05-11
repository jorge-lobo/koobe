package com.jorgelobo.koobe.ui.screen.categories.editor

import com.jorgelobo.koobe.common.extensions.toColor
import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.core.model.updateIfChanged
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryFormState
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryUiStateInternal

// Pure reducer for CategoryEditorIntent.State intents.
// Produces a new Result without side effects; never emits events or launches coroutines.
object CategoryEditorReducer {

    data class Result(
        val form: CategoryFormState,
        val internal: CategoryUiStateInternal
    )

    // Applies the intent to the current form and internal state, returning the updated pair.
    // Fields are only marked as Updated if the new value differs from the base category.
    fun reduce(
        intent: CategoryEditorIntent.State,
        currentForm: CategoryFormState,
        currentInternal: CategoryUiStateInternal,
        baseCategory: Category
    ): Result {

        return when (intent) {

            is CategoryEditorIntent.State.NameChanged -> {
                Result(
                    form = currentForm.copy(
                        name = updateIfChanged(
                            intent.name,
                            baseCategory.name
                        )
                    ),
                    internal = currentInternal
                )
            }

            is CategoryEditorIntent.State.IconSelected -> {
                Result(
                    form = currentForm.copy(
                        icon = updateIfChanged(
                            intent.icon,
                            baseCategory.icon
                        )
                    ),
                    internal = currentInternal
                )
            }

            is CategoryEditorIntent.State.ColorSelected -> {
                Result(
                    form = currentForm.copy(
                        color = updateIfChanged(
                            intent.color,
                            baseCategory.color.toColor()
                        )
                    ),
                    internal = currentInternal
                )
            }

            is CategoryEditorIntent.State.TypeSelected -> {
                Result(
                    form = currentForm.copy(
                        type = updateIfChanged(
                            intent.type,
                            baseCategory.type
                        )
                    ),
                    internal = currentInternal
                )
            }

            is CategoryEditorIntent.State.SubcategoriesChanged -> {
                Result(
                    form = currentForm.copy(
                        subcategories = FieldUpdate.Updated(intent.subcategories)
                    ),
                    internal = currentInternal
                )
            }
        }
    }
}
package com.jorgelobo.koobe.ui.screen.subcategories

import com.jorgelobo.koobe.core.model.updateIfChanged
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.screen.subcategories.state.SubcategoryFormState
import com.jorgelobo.koobe.ui.screen.subcategories.state.SubcategoryUiStateInternal

object SubcategoryEditorReducer {

    data class Result(
        val form: SubcategoryFormState,
        val internal: SubcategoryUiStateInternal
    )

    fun reduce(
        intent: SubcategoryEditorIntent.State,
        currentForm: SubcategoryFormState,
        currentInternal: SubcategoryUiStateInternal,
        baseSubcategory: Subcategory
    ): Result {

        return when (intent) {

            is SubcategoryEditorIntent.State.NameChanged -> {
                Result(
                    form = currentForm.copy(
                        name = updateIfChanged(
                            intent.name,
                            baseSubcategory.name
                        )
                    ),
                    internal = currentInternal
                )
            }

            is SubcategoryEditorIntent.State.IconSelected -> {
                Result(
                    form = currentForm.copy(
                        icon = updateIfChanged(
                            intent.icon,
                            baseSubcategory.icon
                        )
                    ),
                    internal = currentInternal
                )
            }

            is SubcategoryEditorIntent.State.CategoryChanged -> {
                Result(
                    form = currentForm.copy(
                        categoryId = updateIfChanged(
                            intent.categoryId,
                            baseSubcategory.categoryId
                        )
                    ),
                    internal = currentInternal
                )
            }
        }
    }
}
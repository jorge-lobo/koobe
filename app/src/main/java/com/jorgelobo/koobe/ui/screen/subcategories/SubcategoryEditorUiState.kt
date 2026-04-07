package com.jorgelobo.koobe.ui.screen.subcategories

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

data class SubcategoryEditorUiState(
    val config: SubcategoryEditorConfig? = null,
    val category: Category,
    val subcategory: Subcategory,
    val inputState: InputState,
    val initialSnapshot: SubcategoryInitialSnapshot,
    val iconDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val isSaveButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val hasUnsavedChanges: Boolean
        get() = subcategory.name != initialSnapshot.name ||
                subcategory.icon != initialSnapshot.icon ||
                subcategory.categoryId != initialSnapshot.categoryId

    val isValid: Boolean
        get() = subcategory.name.isNotBlank() &&
                subcategory.icon != IconPack.PLACEHOLDER &&
                subcategory.categoryId > 0

    fun headlineRes(isEditMode: Boolean): Int {
        return if (isEditMode) {
            R.string.headline_subcategory_editor
        } else {
            R.string.headline_subcategory_creator
        }
    }

    companion object {
        fun initialEmpty(): SubcategoryEditorUiState {
            val emptySubcategory = Subcategory.empty()
            val emptyCategory = Category.empty()

            return SubcategoryEditorUiState(
                subcategory = emptySubcategory,
                category = emptyCategory,
                inputState = InputState.DEFAULT,
                initialSnapshot = SubcategoryInitialSnapshot(
                    name = "",
                    icon = emptySubcategory.icon,
                    categoryId = emptyCategory.id
                )
            )
        }

        fun initial(
            config: SubcategoryEditorConfig,
            category: Category,
            subcategory: Subcategory
        ): SubcategoryEditorUiState {
            return SubcategoryEditorUiState(
                config = config,
                category = category,
                subcategory = subcategory,
                inputState = InputState.DEFAULT,
                initialSnapshot = SubcategoryInitialSnapshot(
                    name = subcategory.name,
                    icon = subcategory.icon,
                    categoryId = category.id
                )
            )
        }
    }
}

data class SubcategoryInitialSnapshot(
    val name: String,
    val icon: IconPack,
    val categoryId: Int
)
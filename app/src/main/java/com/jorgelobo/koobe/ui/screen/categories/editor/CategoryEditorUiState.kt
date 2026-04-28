package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.isProtected
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType

/**
 * UI state for the Category Editor screen.
 */
data class CategoryEditorUiState(
    val config: CategoryEditorConfig? = null,
    val category: Category,
    val nameInputState: InputState,
    val initialSnapshot: CategoryInitialSnapshot,
    val deleteTarget: CategoryEditorDeleteTarget? = null,
    val iconDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val colorDialog: SelectorDialogState<Color> = SelectorDialogState(),
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val infoDialog: InfoDialogState = InfoDialogState(),
    val isSaveButtonEnabled: Boolean = false,
    val showSnackBar: Boolean = false,
    val isDeleting: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {

    /** True if the category ahs been modified */
    val hasUnsavedChanges: Boolean
        get() = category.name != initialSnapshot.name ||
                category.icon != initialSnapshot.icon ||
                category.color != initialSnapshot.color ||
                category.type != initialSnapshot.type ||
                category.subcategories != initialSnapshot.subcategories

    /** Validates if the category can be saved */
    val isValid: Boolean
        get() = category.name.isNotBlank() &&
                category.icon != IconPack.PLACEHOLDER &&
                category.color.isNotBlank()

    /** Whether the category is protected */
    val isCategoryProtected: Boolean
        get() = category.isProtected()

    /** Whether deletion is allowed for this category */
    val isDeleteEnabled: Boolean
        get() = !isCategoryProtected

    /**
     * Maps the current delete target to its corresponding delete type.
     *
     * Throws an error if no valid delete target is set.
     */
    val deleteType: DeleteType?
        get() = when (deleteTarget) {
            is CategoryEditorDeleteTarget.Category -> DeleteType.CATEGORY
            is CategoryEditorDeleteTarget.Subcategory -> DeleteType.SUBCATEGORY
            else -> null
        }

    /** Returns the headline for the screen based on mode */
    fun headlineRes(isEditMode: Boolean): Int {
        return if (isEditMode) {
            R.string.headline_category_editor
        } else {
            R.string.headline_category_creator
        }
    }

    companion object {

        fun initialEmpty(): CategoryEditorUiState {
            val emptyCategory = Category.empty()

            return CategoryEditorUiState(
                category = emptyCategory,
                nameInputState = InputState.DEFAULT,
                initialSnapshot = CategoryInitialSnapshot(
                    name = "",
                    icon = emptyCategory.icon,
                    color = emptyCategory.color,
                    type = emptyCategory.type,
                    subcategories = emptyCategory.subcategories
                )
            )
        }

        fun initial(
            config: CategoryEditorConfig,
            category: Category
        ): CategoryEditorUiState {
            return CategoryEditorUiState(
                config = config,
                category = category,
                nameInputState = InputState.DEFAULT,
                initialSnapshot = CategoryInitialSnapshot(
                    name = category.name,
                    icon = category.icon,
                    color = category.color,
                    type = category.type,
                    subcategories = category.subcategories
                )
            )
        }
    }
}

/**
 * Snapshot of a category before editing.
 */
data class CategoryInitialSnapshot(
    val name: String,
    val icon: IconPack,
    val color: String,
    val type: TransactionType,
    val subcategories: List<Subcategory>
)
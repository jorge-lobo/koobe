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
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType

/**
 * UI state for the Category Editor screen.
 *
 * [initialSnapshot] captures the persisted category values at load time and is used to determine
 * whether unsaved changes exist.
 */
data class CategoryEditorUiState(
    val config: CategoryEditorConfig = CategoryEditorConfig(),
    val category: Category,
    val nameInputState: InputState,
    val nameError: NameValidationException? = null,
    val initialSnapshot: CategoryInitialSnapshot,
    val deleteTarget: CategoryEditorDeleteTarget? = null,
    val iconDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val colorDialog: SelectorDialogState<Color> = SelectorDialogState(),
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val infoDialog: InfoDialogState = InfoDialogState(),
    val isDeleting: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
) {

    // Valid when name is not blank, icon is not a placeholder, and color is set.
    val isValid: Boolean
        get() = category.name.isNotBlank() &&
                category.icon != IconPack.PLACEHOLDER &&
                category.color.isNotBlank()

    val isCategoryProtected: Boolean
        get() = category.isProtected()

    val isDeleteEnabled: Boolean
        get() = !isCategoryProtected

    val deleteType: DeleteType?
        get() = when (deleteTarget) {
            is CategoryEditorDeleteTarget.Category -> DeleteType.CATEGORY
            is CategoryEditorDeleteTarget.Subcategory -> DeleteType.SUBCATEGORY
            else -> null
        }

    /**
     * Whether the save action should be enabled.
     *
     * Always enabled in create mode (as long as the form is valid).
     * In edit mode, only enabled when the form is valid and differs from [initialSnapshot].
     */
    val isSaveEnabled: Boolean
        get() {
            if (!isValid) return false
            if (!config.isEditMode) return true

            val initial = initialSnapshot

            return category.name != initial.name ||
                    category.icon != initial.icon ||
                    category.color != initial.color ||
                    category.type != initial.type ||
                    category.subcategories != initial.subcategories
        }

    // Returns the screen title string resource based on the current mode.
    fun headlineRes(isEditMode: Boolean): Int {
        return if (isEditMode) {
            R.string.headline_category_editor
        } else {
            R.string.headline_category_creator
        }
    }

    companion object {

        // Initial state before repository data is available; used as StateFlow's initial value.
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
package com.jorgelobo.koobe.ui.screen.subcategories

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.model.subcategory.isProtected
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

/**
 * UI state for the Subcategory Editor screen.
 *
 * [initialSnapshot] captures the persisted subcategory values at load time and is used to determine
 * whether unsaved changes exist.
 */
data class SubcategoryEditorUiState(
    val config: SubcategoryEditorConfig = SubcategoryEditorConfig(),
    val category: Category,
    val subcategory: Subcategory,
    val nameInputState: InputState,
    val nameError: NameValidationException? = null,
    val initialSnapshot: SubcategoryInitialSnapshot,
    val iconDialog: SelectorDialogState<IconPack> = SelectorDialogState(),
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val infoDialog: InfoDialogState = InfoDialogState(),
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {

    // Valid when name is not blank, icon is not a placeholder, and category is not empty
    val isValid: Boolean
        get() = subcategory.name.isNotBlank() &&
                subcategory.icon != IconPack.PLACEHOLDER &&
                subcategory.categoryId > 0

    val isDeleteEnabled: Boolean
        get() = !isSubcategoryProtected

    val isSubcategoryProtected: Boolean
        get() = subcategory.isProtected()

    /**
     * Whether the save action should be enabled.
     *
     * Always enabled in create mode (as long as the form is valid).
     * In edit mode, only enabled when the form is valid and differs from [initialSnapshot].
     */
    val isSaveEnabled: Boolean
        get() {
            if (!isValid) return false

            return if (config.isEditMode) {
                val initial = initialSnapshot

                subcategory.name != initial.name ||
                        subcategory.icon != initial.icon ||
                        subcategory.categoryId != initial.categoryId
            } else {
                true
            }
        }

    // Returns the screen title string resource based on the current mode.
    fun headlineRes(isEditMode: Boolean): Int {
        return if (isEditMode) {
            R.string.headline_subcategory_editor
        } else {
            R.string.headline_subcategory_creator
        }
    }

    companion object {

        // Initial state before repository data is available; used as StateFlow's initial value.
        fun initialEmpty(): SubcategoryEditorUiState {
            val emptySubcategory = Subcategory.empty()
            val emptyCategory = Category.empty()

            return SubcategoryEditorUiState(
                subcategory = emptySubcategory,
                category = emptyCategory,
                nameInputState = InputState.DEFAULT,
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
                nameInputState = InputState.DEFAULT,
                initialSnapshot = SubcategoryInitialSnapshot(
                    name = subcategory.name,
                    icon = subcategory.icon,
                    categoryId = category.id
                )
            )
        }
    }
}

/**
 * Snapshot of the initial subcategory state used to detect changes.
 */
data class SubcategoryInitialSnapshot(
    val name: String,
    val icon: IconPack,
    val categoryId: Int
)
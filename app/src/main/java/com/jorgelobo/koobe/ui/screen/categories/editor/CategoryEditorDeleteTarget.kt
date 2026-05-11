package com.jorgelobo.koobe.ui.screen.categories.editor

sealed interface CategoryEditorDeleteTarget {
    data object None : CategoryEditorDeleteTarget
    data object Category : CategoryEditorDeleteTarget
    data class Subcategory(val id: Int) : CategoryEditorDeleteTarget
}
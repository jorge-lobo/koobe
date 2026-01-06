package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import kotlinx.serialization.Serializable

@Serializable
data class ShortcutEditorConfig(
    val shortcutId: Int? = null,
    val categoryId: Int? = null
) {
    val isEditMode: Boolean
        get() = shortcutId != null
}
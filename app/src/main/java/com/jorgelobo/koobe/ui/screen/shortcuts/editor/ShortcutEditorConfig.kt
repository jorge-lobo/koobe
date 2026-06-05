package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import kotlinx.serialization.Serializable

@Serializable
sealed interface ShortcutEditorConfig {
    @Serializable
    data class Create(val categoryId: Int) : ShortcutEditorConfig

    @Serializable
    data class Edit(val shortcutId: Int) : ShortcutEditorConfig
}
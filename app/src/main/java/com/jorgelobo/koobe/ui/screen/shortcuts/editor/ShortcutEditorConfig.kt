package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import kotlinx.serialization.Serializable

/**
 * Configuration for the shortcut editor screen, defining whether the screen is used to create a
 * new shortcut or edit an existing one.
 *
 * @property Create Configuration for adding a new shortcut to a specific category.
 * @property Edit Configuration for modifying an existing shortcut identified by its ID.
 */
@Serializable
sealed interface ShortcutEditorConfig {
    @Serializable
    data class Create(val categoryId: Int) : ShortcutEditorConfig

    @Serializable
    data class Edit(val shortcutId: Int) : ShortcutEditorConfig
}
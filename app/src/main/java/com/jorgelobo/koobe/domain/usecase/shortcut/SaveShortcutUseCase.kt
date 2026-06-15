package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import javax.inject.Inject

class SaveShortcutUseCase @Inject constructor(
    private val insertShortcut: InsertShortcutUseCase,
    private val updateShortcut: UpdateShortcutUseCase
) {
    suspend operator fun invoke(
        shortcut: Shortcut,
        config: ShortcutEditorConfig
    ) {
        when (config) {
            is ShortcutEditorConfig.Create -> insertShortcut(shortcut)
            is ShortcutEditorConfig.Edit -> updateShortcut(shortcut)
        }
    }
}
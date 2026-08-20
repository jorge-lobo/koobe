package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.validation.NameValidationUseCase
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.utils.date.DateUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SaveShortcutUseCase @Inject constructor(
    private val insertShortcut: InsertShortcutUseCase,
    private val updateShortcut: UpdateShortcutUseCase,
    private val nameValidation: NameValidationUseCase,
    private val shortcutRepository: ShortcutRepository
) {
    suspend operator fun invoke(
        shortcut: Shortcut,
        config: ShortcutEditorConfig
    ) {

        val allShortcuts = shortcutRepository.getAllShortcuts().first()

        nameValidation.validate(
            name = shortcut.name,
            currentId = shortcut.id,
            existingItems = allShortcuts,
            extractId = { it.id },
            extractName = { it.name },
            normalize = { input -> input.trim().lowercase() }
        )

        val originalShortcut =
            when (config) {
                is ShortcutEditorConfig.Create -> null
                is ShortcutEditorConfig.Edit -> shortcutRepository.getShortcutById(config.shortcutId)
            }

        val shouldResetExecutionDate =
            (config is ShortcutEditorConfig.Create && shortcut.repeat) ||
                    (originalShortcut != null && shortcut.repeat &&
                            (!originalShortcut.repeat || originalShortcut.period != shortcut.period))

        val shortcutToSave =
            if (shouldResetExecutionDate) {
                shortcut.copy(lastExecutionDate = DateUtils.currentDate)
            } else {
                shortcut
            }

        when (config) {
            is ShortcutEditorConfig.Create -> insertShortcut(shortcutToSave)
            is ShortcutEditorConfig.Edit -> updateShortcut(shortcutToSave)
        }
    }
}
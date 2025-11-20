package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.ShortcutRepository

class DeleteShortcutUseCase(
    private val repository: ShortcutRepository
) {
    suspend operator fun invoke(shortcut: Shortcut) {
        repository.deleteShortcut(shortcut)
    }
}
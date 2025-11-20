package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.repository.ShortcutRepository

class GetAllShortcutsUseCase(
    private val repository: ShortcutRepository
) {
    operator fun invoke() = repository.getAllShortcuts()
}
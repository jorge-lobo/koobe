package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.ShortcutRepository

class GetShortcutByIdUseCase(
    private val repository: ShortcutRepository
) {
    suspend operator fun invoke(id: Int): Shortcut? =
        repository.getShortcutById(id)
}
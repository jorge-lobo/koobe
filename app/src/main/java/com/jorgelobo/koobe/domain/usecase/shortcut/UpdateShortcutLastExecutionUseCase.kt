package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import java.util.Date
import javax.inject.Inject

class UpdateShortcutLastExecutionUseCase @Inject constructor(
    private val repository: ShortcutRepository
) {
    suspend operator fun invoke(
        shortcut: Shortcut,
        date: Date
    ) {
        repository.updateShortcut(
            shortcut.copy(
                lastExecutionDate = date
            )
        )
    }
}
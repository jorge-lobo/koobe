package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetDueShortcutsUseCase @Inject constructor(
    private val repository: ShortcutRepository,
    private val shouldExecuteShortcut: ShouldExecuteShortcutUseCase
) {
    suspend operator fun invoke(): List<Shortcut> =
        repository
            .getAllShortcuts()
            .first()
            .filter(shouldExecuteShortcut::invoke)
}
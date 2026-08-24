package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import javax.inject.Inject

class GetAllShortcutsUseCase @Inject constructor(
    private val repository: ShortcutRepository
) {
    operator fun invoke() = repository.getAllShortcuts()
}
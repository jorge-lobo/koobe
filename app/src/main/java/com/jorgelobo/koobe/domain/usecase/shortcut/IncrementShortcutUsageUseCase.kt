package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import javax.inject.Inject

class IncrementShortcutUsageUseCase @Inject constructor(
    private val repository: ShortcutRepository
) {
    suspend operator fun invoke(shortcutId: Int) {
        repository.incrementShortcutUsageCount(shortcutId)
    }
}
package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.repository.ShortcutRepository

class GetShortcutsByCategoryIdUseCase(
    private val repository: ShortcutRepository
) {
    operator fun invoke(categoryId: Int) = repository.getShortcutsByCategoryId(categoryId)
}
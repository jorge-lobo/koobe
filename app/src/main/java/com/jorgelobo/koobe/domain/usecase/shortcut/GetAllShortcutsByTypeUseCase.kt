package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllShortcutsByTypeUseCase @Inject constructor(
    private val repository: ShortcutRepository
) {
    operator fun invoke(type: TransactionType): Flow<List<Shortcut>> =
        repository.getShortcutsByType(type)
}
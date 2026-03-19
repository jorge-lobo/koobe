package com.jorgelobo.koobe.domain.repository

import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import kotlinx.coroutines.flow.Flow

interface ShortcutRepository {
    fun getAllShortcuts(): Flow<List<Shortcut>>
    fun getShortcutsByCategoryId(categoryId: Int): Flow<List<Shortcut>>
    suspend fun getShortcutById(shortcutId: Int): Shortcut?
    suspend fun insertShortcut(shortcut: Shortcut)
    suspend fun updateShortcut(shortcut: Shortcut)
    suspend fun deleteShortcut(shortcut: Shortcut)
    fun getShortcutByIdFlow(id: Int): Flow<Shortcut?>
}
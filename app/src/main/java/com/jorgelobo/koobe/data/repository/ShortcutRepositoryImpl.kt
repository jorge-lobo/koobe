package com.jorgelobo.koobe.data.repository

import com.jorgelobo.koobe.data.local.dao.ShortcutDao
import com.jorgelobo.koobe.data.mapper.toDomain
import com.jorgelobo.koobe.data.mapper.toEntity
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShortcutRepositoryImpl @Inject constructor(
    private val dao: ShortcutDao
) : ShortcutRepository {

    override fun getAllShortcuts(): Flow<List<Shortcut>> =
        dao.getAll().map { list ->
            list.map { it.toDomain() }
        }

    override fun getShortcutsByCategoryId(categoryId: Int): Flow<List<Shortcut>> =
        dao.getByCategoryId(categoryId).map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getShortcutById(shortcutId: Int): Shortcut? =
        dao.getById(shortcutId)?.toDomain()

    override suspend fun insertShortcut(shortcut: Shortcut) {
        dao.insert(shortcut.toEntity())
    }

    override suspend fun updateShortcut(shortcut: Shortcut) {
        dao.update(shortcut.toEntity())
    }

    override suspend fun deleteShortcut(shortcut: Shortcut) {
        dao.delete(shortcut.toEntity())
    }

    override fun getShortcutByIdFlow(id: Int): Flow<Shortcut?> =
        dao.getByIdFlow(id).map { it?.toDomain() }
}
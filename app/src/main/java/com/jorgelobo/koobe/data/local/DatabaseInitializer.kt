package com.jorgelobo.koobe.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jorgelobo.koobe.data.local.dao.CategoryDao
import com.jorgelobo.koobe.data.local.dao.SubcategoryDao
import com.jorgelobo.koobe.data.local.defaults.CategoryDefaults
import com.jorgelobo.koobe.data.local.defaults.SubcategoryDefaults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseInitializer(
    private val categoryDaoProvider: () -> CategoryDao,
    private val subcategoryDaoProvider: () -> SubcategoryDao
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            val categoryDao = categoryDaoProvider()
            val subcategoryDao = subcategoryDaoProvider()

            categoryDao.insertAll(CategoryDefaults.categories)
            subcategoryDao.insertAll(SubcategoryDefaults.subcategories)
        }
    }
}
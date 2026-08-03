package com.jorgelobo.koobe.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                ALTER TABLE shortcuts
                ADD COLUMN usageCount INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
            )
        }
    }

    val ALL = arrayOf(
        MIGRATION_1_2
    )
}
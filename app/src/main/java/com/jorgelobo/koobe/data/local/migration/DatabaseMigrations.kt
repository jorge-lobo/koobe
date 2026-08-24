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

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                ALTER TABLE shortcuts
                ADD COLUMN lastExecutionDate INTEGER
                """.trimIndent()
            )
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE transactions_new (
                id INTEGER NOT NULL,
                categoryId INTEGER NOT NULL,
                subcategoryId INTEGER,
                shortcutId INTEGER,
                date INTEGER NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL,
                amount REAL NOT NULL,
                paymentMethod TEXT NOT NULL,
                currency TEXT NOT NULL,
                PRIMARY KEY(id),
                FOREIGN KEY(categoryId)
                    REFERENCES categories(id)
                    ON DELETE CASCADE,
                FOREIGN KEY(subcategoryId)
                    REFERENCES subcategories(id)
                    ON DELETE SET NULL,
                FOREIGN KEY(shortcutId)
                    REFERENCES shortcuts(id)
                    ON DELETE SET NULL
            )
            """.trimIndent()
            )

            db.execSQL(
                """
            INSERT INTO transactions_new (
                id,
                categoryId,
                subcategoryId,
                date,
                description,
                type,
                amount,
                paymentMethod,
                currency
            )
            SELECT
                id,
                categoryId,
                subcategoryId,
                date,
                description,
                type,
                amount,
                paymentMethod,
                currency
            FROM transactions
            """.trimIndent()
            )

            db.execSQL("DROP TABLE transactions")

            db.execSQL(
                """
            ALTER TABLE transactions_new
            RENAME TO transactions
            """.trimIndent()
            )
        }
    }

    val ALL = arrayOf(
        MIGRATION_1_2,
        MIGRATION_2_3,
        MIGRATION_3_4
    )
}
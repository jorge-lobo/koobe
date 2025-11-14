package com.jorgelobo.koobe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconName: String,
    val color: String,
    val type: TransactionType
)
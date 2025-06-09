package com.example.prog7313_part3.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets", foreignKeys = [
        androidx.room.ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val month: Int,  // 1-12
    val year: Int,
    val minAmount: Double,
    val maxAmount: Double,
)
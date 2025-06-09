package com.example.prog7313_part3.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val passwordHash: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val profilePicturePath: String? = null
)

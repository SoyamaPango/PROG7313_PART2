package com.example.prog7313_part_3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity(


    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var username: String,
    var email: String,
    var birthDate: String,
    var phone: String,
    var password: String,
)
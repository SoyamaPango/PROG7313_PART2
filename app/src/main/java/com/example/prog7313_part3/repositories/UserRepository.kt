package com.example.prog7313_part3.repositories

import com.example.prog7313_part3.daos.UserDao
import com.example.prog7313_part3.entities.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }

    suspend fun loginUser(email: String, password: String): User? {
        val user = userDao.getUserByEmailForLogin(email) ?: return null

        // Compare password with stored hash using BCrypt
        val passwordMatches = at.favre.lib.crypto.bcrypt.BCrypt.verifyer()
            .verify(password.toCharArray(), user.passwordHash)
            .verified

        return if (passwordMatches) user else null
    }
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}
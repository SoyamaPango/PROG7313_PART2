package com.example.prog7313_part3

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "user_session", Context.MODE_PRIVATE
    )

    companion object {
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
    }

    fun saveUserId(userId: Int) {
        sharedPreferences.edit {
            putInt(USER_ID, userId)
        }
    }

    fun saveUserEmail(email: String) {
        sharedPreferences.edit {
            putString(USER_EMAIL, email)
        }
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(USER_ID, -1)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(USER_EMAIL, null)
    }


    fun clearSession() {
        sharedPreferences.edit {
            clear()
        }
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != -1
    }

    fun saveUserName(name: String) {
        sharedPreferences.edit { putString("USER_NAME", name) }
    }

    fun getUserName(): String? {
        return sharedPreferences.getString("USER_NAME", null)
    }
}
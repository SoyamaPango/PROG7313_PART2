package com.example.prog7313_part_3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import com.example.prog7313_part_3.data.AppDatabase
import com.example.prog7313_part_3.data.UserDAO


class LoginPageActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "users.db").build()
        userDao = db.userDAO()

        val Loginbtn = findViewById<Button>(R.id.btnLogin)
        Loginbtn.setOnClickListener {
            onLoginButtonClick()
        }
        val backbtn = findViewById<Button>(R.id.btnBack)
        backbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onLoginButtonClick() {
        val userName: String = findViewById<EditText>(R.id.edtUsername).text.toString()
        val password: String = findViewById<EditText>(R.id.edtPassword).text.toString()

        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            return
        }

        val hashedPassword = hashPassword(password)

        lifecycleScope.launch(Dispatchers.IO) {
            val user = userDao.getUserByUsernameAndPassword(userName, hashedPassword)

            launch(Dispatchers.Main) {
                if (user != null) {
                    Toast.makeText(this@LoginPageActivity, "Login successful!", Toast.LENGTH_SHORT)
                        .show()
                    saveUserId(user.id)
                    val intent = Intent(this@LoginPageActivity, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LoginPageActivity, "Invalid username or password", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun hashPassword(password: String): String { //not required
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(password.toByteArray())
            hashBytes.fold("") { str, it -> str + "%02x".format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            "" // Handle hashing errors appropriately
        }
    }

    private fun saveUserId(userId: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            val userPreferences = UserPreferences(this@LoginPageActivity)
            userPreferences.saveUserId(userId)
        }
    }
}





package com.example.prog7313_part3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prog7313_part3.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch
import com.example.prog7313_part3.entities.User
import com.example.prog7313_part3.repositories.UserRepository


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repository and session manager
        val database = AppDatabase.getDatabase(applicationContext)
        userRepository = UserRepository(database.userDao())
        sessionManager = SessionManager(applicationContext)

        // Set up signup button click
        binding.buttonSignup.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (validateInputs(email, password)) {
                registerUser(email, password)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        // Validate email
        val emailValidation = ValidationUtils.validateEmail(email)
        if (!emailValidation.isValid) {
            binding.editTextEmail.error = emailValidation.errorMessage
            return false
        }

        // Validate password
        val passwordValidation = ValidationUtils.validatePassword(password)
        if (!passwordValidation.isValid) {
            binding.editTextPassword.error = passwordValidation.errorMessage
            return false
        }

        return true
    }
    private fun startProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun registerUser(email: String, password: String) {
        lifecycleScope.launch {
            try {
                // Check if user already exists
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    // User already exists
                    Toast.makeText(
                        this@SignUpActivity,
                        "Email already registered", Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                // Create new user with hashed password
                val newUser = User(
                    email = email,
                    passwordHash = hashPassword(password)
                )

                // Insert user and get ID
                val userId = userRepository.insertUser(newUser)

                // Save user session
                sessionManager.saveUserId(userId.toInt())
                sessionManager.saveUserEmail(email)

                Toast.makeText(
                    this@SignUpActivity,
                    "Registration successful!", Toast.LENGTH_SHORT
                ).show()

                startProfileActivity()
            } catch (e: Exception) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Registration failed: ${e.message}", Toast.LENGTH_LONG
                ).show()

                Log.d("SignUpActivity", "Registration error: ${e.message}")
            }
        }
    }

    private fun hashPassword(password: String): String {
        return at.favre.lib.crypto.bcrypt.BCrypt.withDefaults()
            .hashToString(12, password.toCharArray())
    }


    private fun startDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
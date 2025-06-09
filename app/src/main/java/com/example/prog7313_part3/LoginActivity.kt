package com.example.prog7313_part3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prog7313_part3.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.prog7313_part3.repositories.UserRepository

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repository and session manager
        val database = AppDatabase.getDatabase(applicationContext)
        userRepository = UserRepository(database.userDao())
        sessionManager = SessionManager(applicationContext)

        // Set up login button click
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (validateInputs(email, password)) {
                loginUser(email, password)
            }
        }

        // Set up register button click
        binding.buttonSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        // Validate email
        val emailValidation = ValidationUtils.validateEmail(email)
        if (!emailValidation.isValid) {
            binding.editTextEmail.error = emailValidation.errorMessage
            return false
        }

        // For login, we might want to keep password validation simpler
        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password cannot be empty"
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            try {
                // Get user by email
                val user = userRepository.getUserByEmail(email)

                if (user == null) {
                    Toast.makeText(
                        this@LoginActivity,
                        "User not found", Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                // Verify password
                val result = BCrypt.verifyer().verify(
                    password.toCharArray(),
                    user.passwordHash
                )

                if (result.verified) {
                    // Save user session
                    sessionManager.saveUserId(user.id)
                    sessionManager.saveUserEmail(user.email)
                    val fName = user.firstName
                    val lName = user.lastName
                    sessionManager.saveUserName("$fName $lName")

                    Toast.makeText(
                        this@LoginActivity,
                        "Login successful!", Toast.LENGTH_SHORT
                    ).show()

                    startDashboardActivity()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Incorrect password", Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Login failed: ${e.message}", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
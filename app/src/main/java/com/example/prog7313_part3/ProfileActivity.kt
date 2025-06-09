package com.example.prog7313_part3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prog7313_part3.databinding.ActivityProfileBinding
import com.example.prog7313_part3.entities.User
import com.example.prog7313_part3.repositories.UserRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager
    private var selectedImageUri: Uri? = null
    private var currentUser: User? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.profileImageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repository and session manager
        val database = AppDatabase.getDatabase(applicationContext)
        userRepository = UserRepository(database.userDao())
        sessionManager = SessionManager(applicationContext)

        // Setup change photo button
        binding.buttonChangePhoto.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Setup save button
        binding.buttonSaveProfile.setOnClickListener {
            saveUserProfile()
        }

        // Load existing user data
        loadUserData()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                val userId = sessionManager.getUserId()
                if (userId != -1) {
                    // Get current user from database
                    val user = userRepository.getUserById(userId)

                    user?.let {
                        currentUser = it

                        // Set first name and last name fields
                        binding.editTextFirstName.setText(it.firstName)
                        binding.editTextLastName.setText(it.lastName)

                        // Update page title to show it's profile editing
                        binding.textProfileTitle.text = "Edit Your Profile"

                        // Load profile picture if available
                        it.profilePicturePath?.let { path ->
                            if (path.isNotEmpty()) {
                                val imageFile = File(path)
                                if (imageFile.exists()) {
                                    val imageUri = Uri.fromFile(imageFile)
                                    binding.profileImageView.setImageURI(imageUri)
                                }
                            }
                        }
                    } ?: run {
                        // Pre-fill with email username if no user found (for new users)
                        val userEmail = sessionManager.getUserEmail() ?: ""
                        val defaultName = userEmail.substringBefore("@")
                        binding.editTextFirstName.setText(defaultName)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Failed to load profile: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun saveUserProfile() {
        val firstName = binding.editTextFirstName.text.toString().trim()
        val lastName = binding.editTextLastName.text.toString().trim()

        if (firstName.isEmpty()) {
            binding.editTextFirstName.error = "First name is required"
            return
        }

        lifecycleScope.launch {
            try {
                val userId = sessionManager.getUserId()
                if (userId != -1) {
                    if (currentUser == null) {
                        currentUser = userRepository.getUserById(userId)
                    }

                    currentUser?.let {
                        // Create updated user with new values
                        val updatedUser = User(
                            id = it.id,
                            email = it.email,
                            passwordHash = it.passwordHash,
                            firstName = firstName,
                            lastName = lastName,
                            profilePicturePath = if (selectedImageUri != null) {
                                saveProfileImage(selectedImageUri!!, userId)
                            } else {
                                it.profilePicturePath
                            }
                        )

                        // Update user in database
                        userRepository.updateUser(updatedUser)

                        // Save the name to session for easy access
                        sessionManager.saveUserName("$firstName $lastName")

                        Toast.makeText(
                            this@ProfileActivity,
                            "Profile updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to Dashboard
                        startDashboardActivity()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ProfileActivity,
                    "Failed to update profile: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun saveProfileImage(uri: Uri, userId: Int): String {
        val inputStream = contentResolver.openInputStream(uri)
        val profileImagesDir = File(filesDir, "profile_images")
        if (!profileImagesDir.exists()) {
            profileImagesDir.mkdirs()
        }

        val imageFile = File(profileImagesDir, "user_${userId}.jpg")
        val outputStream = FileOutputStream(imageFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return imageFile.absolutePath
    }

    private fun startDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
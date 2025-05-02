package vcmsa.projects.tcss

import android.os.Bundle
import android.widget.EditText
import android.content.Intent
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.tcss.data.UserDAO
import vcmsa.projects.tcss.data.UserEntity
import java.security.MessageDigest


class SignUpPageActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "users.db").build()
        userDao = db.userDAO()

        val signUpbtn = findViewById<Button>(R.id.btnSignUp)
        signUpbtn.setOnClickListener {
            onSignUpClick()
        }
        val backbtn = findViewById<Button>(R.id.btnBack)
        backbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onSignUpClick() {

        val name: String =
            findViewById<EditText>(R.id.edtUsername).text.toString() //store to database


        val email = findViewById<EditText>(R.id.edtEmail).text.toString() //store to database

        val birthDate =
            findViewById<EditText>(R.id.edtBirthdate).text.toString() //store to database

        val phone = findViewById<EditText>(R.id.edtPhoneNo).text.toString() //store to database

        val password =
            findViewById<EditText>(R.id.edtPassword).text.toString() //store to database

        val confirmPassword =
            findViewById<EditText>(R.id.edtConfirmPassword).text.toString() //store to database

        // Validate empty fields
        if (name.isEmpty() || email.isEmpty() ||
            birthDate.isEmpty() || phone.isEmpty() || password.isEmpty()
        ) {
            showToast("All fields are required")
            return
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address")
            return
        }

        // Validate password length
        if (password.length < 8) {
            showToast("Password must be at least 8 characters long")
            return
        }

        // Validate password match
        if (password != confirmPassword) {
            showToast("Passwords do not match")
            return
        }

        // Validate phone number (basic format)
        if (!phone.matches(Regex("^[0-9]{10}$"))) {
            showToast("Please enter a valid 10-digit phone number")
            return
        }

        val hashedPassword = hashPassword(password)

        // Save to Database
        lifecycleScope.launch(Dispatchers.IO) {
            val newUser = UserEntity(0, name, email, birthDate, phone, hashedPassword)
            userDao.insertUser(newUser)

            launch(Dispatchers.Main) {
                showToast("Sign-up successful!")
                saveUserId(newUser.id)
                GoToDashboard()
            }

        }
    }

    private fun GoToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun hashPassword(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(password.toByteArray())
            hashBytes.fold("") { str, it -> str + "%02x".format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun saveUserId(userId: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            val userPreferences = UserPreferences(this@SignUpPageActivity)
            userPreferences.saveUserId(userId)
        }
    }

}

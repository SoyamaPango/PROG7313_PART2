package vcmsa.projects.tcss

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import androidx.activity.ComponentActivity
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withContext
import java.security.MessageDigest


class LoginPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val Loginbtn = findViewById<Button>(R.id.btnLogin)
        Loginbtn.setOnClickListener {
            onLoginButtonClick()
        }
    }

    private fun onLoginButtonClick() {
        val userName: String = findViewById<EditText>(R.id.edtUsername).text.toString()
        val password: String = findViewById<EditText>(R.id.edtPassword).text.toString()

        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            return
        }

        val hashedPaswword = hashPassword(password)

        //database search fix as needed
        val user = withContext(Dispatchers.IO) {
            database.userDao().getUserByUsernameAndPassword(userName, hashedPaswword)
        }

        if (user != null) {
            // User found, login successful
            Toast.makeText(this@LoginPageActivity, "Login successful!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@LoginPageActivity, DashboardActivity::class.java)
            startActivity(intent)
        } else {
            // User not found
            Toast.makeText(
                this@LoginPageActivity,
                "Invalid username or password",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun hashPassword(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(password.toByteArray())
            hashBytes.fold("") { str, it -> str + "%02x".format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            "" // Handle hashing errors appropriately
        }
    }
}
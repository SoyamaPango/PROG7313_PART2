package vcmsa.projects.tcss

import android.os.Bundle
import android.widget.EditText
import android.content.Intent
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity


class SignUpPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        val signUpbtn = findViewById<Button>(R.id.btnSignUp)
        signUpbtn.setOnClickListener {
            onSignUpClick()
        }
    }

    private fun onSignUpClick() {

        val name: String = findViewById<EditText>(R.id.edtFName).text.toString() //store to database

        val lastName =
            findViewById<EditText>(R.id.edtSurname).text.toString() //store to database

        val email = findViewById<EditText>(R.id.edtEmail).text.toString() //store to database

        val birthDate =
            findViewById<EditText>(R.id.edtBirthdate).text.toString() //store to database

        val phone = findViewById<EditText>(R.id.edtPhoneNo).text.toString() //store to database

        val password =
            findViewById<EditText>(R.id.edtPassword).text.toString() //store to database

        val confirmPassword =
            findViewById<EditText>(R.id.edtConfirmPassword).text.toString() //store to database

        // Validate empty fields
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
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

        // If all validations pass, proceed with sign up
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

}

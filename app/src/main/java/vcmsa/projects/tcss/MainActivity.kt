package vcmsa.projects.tcss

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Handle the splash screen transition
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            val loginIntent = Intent(this, LoginPageActivity::class.java)
            startActivity(loginIntent)
        }

        // Sign up button click listener
        btnSignUp.setOnClickListener {
            val signUpIntent = Intent(this, SignUpPageActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}
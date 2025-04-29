package vcmsa.projects.tcss

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        val btnLogin : Button = findViewById(R.id.btnLogin)
        val btnSignUp : Button = findViewById(R.id.btnSignUp)


        btnLogin.setOnClickListener {
            // Code to execute when the button is clicked
            val LoginIntent = Intent(
                this,
                LoginPageActivity::class.java
            ) // Replace SecondActivity with the name of your target Activity
            startActivity(LoginIntent)
        }

        btnSignUp.setOnClickListener {
            // Code to execute when the button is clicked
            val SignUpIntent = Intent(
                this,
                SignUpPageActivity::class.java
            )
            startActivity(SignUpIntent)
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}
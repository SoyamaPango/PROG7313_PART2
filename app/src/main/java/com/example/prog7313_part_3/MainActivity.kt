package com.example.prog7313_part_3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
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

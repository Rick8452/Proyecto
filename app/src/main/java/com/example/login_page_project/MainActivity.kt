package com.example.login_page_project

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var email_input :EditText
    lateinit var passwordInput :EditText
    lateinit var loginBtn : Button
    lateinit var registro: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email_input = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        registro = findViewById(R.id.registro)

        loginBtn.setOnClickListener{
            val email = email_input.text.toString()
            val password = passwordInput.text.toString()
            Log.i("Test Credentials", "Username : $email and Password : $password")
        }


        registro.setOnClickListener{
            val intent: Intent = Intent(this, Registro:: class.java)
            startActivity(intent)
        }

    }
}
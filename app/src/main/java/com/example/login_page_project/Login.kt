package com.example.login_page_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("login.php")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): Response<String>

}
class Login : AppCompatActivity() {

    lateinit var email_input: EditText
    lateinit var passwordInput: EditText
    lateinit var loginBtn: Button
    lateinit var registro: TextView
    lateinit var apiServices: ApiService
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        email_input = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        registro = findViewById(R.id.registro)
        requestQueue = Volley.newRequestQueue(this)

        checkLoginStatus()

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())


        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.35:8080/login/") // URL base de tu servidor
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()


        apiServices = retrofit.create(ApiService::class.java)

        loginBtn.setOnClickListener {
            val email = email_input.text.toString().trim()
            val password = passwordInput.text.toString().trim()


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            lifecycleScope.launch {
                try {
                    val response = apiServices.login(email, password)
                    if (response.isSuccessful) {
                        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean("is_logged_in", true)
                            putString("email", email)
                            apply()
                        }
                        val intent = Intent(this@Login, Home::class.java)
                        startActivity(intent)
                        finish()
                    } else {

                        Toast.makeText(applicationContext, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {

                    Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registro.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
    }
    private fun checkLoginStatus() {
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }
    }
    object UserManager {
        private const val PREF_NAME = "user_prefs"
        private const val USER_ID_KEY = "user_id"

        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        var userId: String?
            get() = sharedPreferences.getString(USER_ID_KEY, null)
            set(value) {
                sharedPreferences.edit().putString(USER_ID_KEY, value).apply()
            }
    }
}
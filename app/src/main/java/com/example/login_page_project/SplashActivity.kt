package com.example.login_page_project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide

class SplashActivity : ComponentActivity() {

    private val splashDuration: Long = 2000 // Duración del splash screen en milisegundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Cargar el GIF usando Glide
        val splashImage = findViewById<ImageView>(R.id.splash_image)
        Glide.with(this)
            .asGif()
            .load(R.drawable.splash_animation) // Nombre del archivo GIF en res/drawable
            .into(splashImage)

        // Retardo antes de redirigir a la pantalla principal
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            finish()
        }, splashDuration)
    }
    override fun onRestart() {
        super.onRestart()
        val intent = Intent(this, Login::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }
}

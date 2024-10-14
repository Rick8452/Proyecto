package com.example.login_page_project

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase App
        FirebaseApp.initializeApp(this)

        // Probar conexión a Firebase Auth
        val auth = FirebaseAuth.getInstance()
        if (auth != null) {
            Log.d("FirebaseTest", "Conectado a Firebase Auth")
        } else {
            Log.d("FirebaseTest", "No se pudo conectar a Firebase Auth")
        }

        // Probar conexión a Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("test").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseTest", "Conectado a Firebase Firestore")
            } else {
                Log.d("FirebaseTest", "No se pudo conectar a Firebase Firestore: ${task.exception}")
            }
        }
    }
}

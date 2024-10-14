package com.example.login_page_project

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.login_page_project.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var requestQueue: RequestQueue

    private var fotoUsuario: Bitmap? = null
    private var fotoSubida: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        requestQueue = Volley.newRequestQueue(this)

        // Cargar datos del usuario actual
        cargarDatosUsuario()

        // Manejar clic en botón de subir foto
        binding.btnSubirFoto.setOnClickListener {
            abrirGaleria()
        }

        // Manejar clic en botón de guardar cambios
        binding.btnGuardarCambios.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarDatosUsuario() {
        // Aquí cargarías los datos del usuario desde tu base de datos o SharedPreferences
        val usuario = obtenerUsuarioActual()

        binding.editTextNombre.setText(usuario.nombre)
        binding.editTextApellidos.setText(usuario.apellidos)
        binding.editTextEdad.setText(usuario.edad.toString())
        binding.editTextDireccion.setText(usuario.direccion)
        binding.editTextCodigoPostal.setText(usuario.codigoPostal)
        binding.editTextMunicipio.setText(usuario.municipio)
        binding.editTextEstado.setText(usuario.estado)
        binding.editTextTelefono.setText(usuario.telefono)

        // Cargar la foto del usuario si existe
        cargarFotoUsuario(usuario.fotoUsuario)
    }

    private fun cargarFotoUsuario(fotoUsuario: String?) {
        if (fotoUsuario != null) {
            // Si tienes la foto del usuario, muéstrala en imageViewFoto
            Glide.with(this)
                .load("http://192.168.100.35:8080/login/$fotoUsuario")
                .placeholder(R.drawable.placeholder_image) // Placeholder mientras carga la imagen
                .error(R.drawable.placeholder_image) // Placeholder en caso de error al cargar la imagen
                .into(binding.imageViewFoto)
            binding.imageViewFoto.visibility = ImageView.VISIBLE
        } else {
            // Si no tienes la foto del usuario, muestra un placeholder
            binding.imageViewFoto.setImageResource(R.drawable.placeholder_image)
            binding.imageViewFoto.visibility = ImageView.VISIBLE
        }
    }


    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }
    
    private fun guardarCambios() {
        val nombre = binding.editTextNombre.text.toString().trim()
        val apellidos = binding.editTextApellidos.text.toString().trim()
        val edad = binding.editTextEdad.text.toString().toIntOrNull()
        val direccion = binding.editTextDireccion.text.toString().trim()
        val codigoPostal = binding.editTextCodigoPostal.text.toString().trim()
        val municipio = binding.editTextMunicipio.text.toString().trim()
        val estado = binding.editTextEstado.text.toString().trim()
        val telefono = binding.editTextTelefono.text.toString().trim()

        if (nombre.isEmpty() || apellidos.isEmpty() || edad == null || direccion.isEmpty() ||
            codigoPostal.isEmpty() || municipio.isEmpty() || estado.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Subir la foto del usuario si se ha seleccionado una nueva
        if (fotoSubida) {
            subirFotoUsuario()
        }

        // Aquí implementarías la lógica para guardar los cambios en la base de datos
        // o enviar los datos al servidor. A modo de ejemplo, mostraré un mensaje.
        // Por ahora, simplemente mostrar un mensaje
        Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
    }

    private fun subirFotoUsuario() {
        val url = "http://192.168.100.35:8080/login/upload_photo.php" // URL de tu script PHP para subir la foto
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                // Procesar la respuesta si es necesario
                Toast.makeText(this, "Foto subida correctamente", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(this, "Error al subir la foto", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                // Ajusta los parámetros según tu implementación en PHP
                params["idusuario"] = "1" // Por ejemplo, el ID del usuario actual
                params["foto_usuario"] = "nombre_foto.jpg" // Nombre de la foto en el servidor
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    val imageUri = data?.data
                    fotoUsuario = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    binding.imageViewFoto.setImageBitmap(fotoUsuario)
                    binding.imageViewFoto.visibility = ImageView.VISIBLE
                    fotoSubida = true
                }
            }
        }
    }

    private fun obtenerUsuarioActual(): Usuario {
        // Aquí deberías implementar la lógica para obtener los datos del usuario actual
        // desde tu base de datos local o servicios web. A modo de ejemplo, crearé un usuario ficticio.
        return Usuario(
            1, // idusuario (ejemplo)
            "Juan", // nombre
            "Pérez", // apellidos
            30, // edad
            "Calle Principal 123", // dirección
            "12345", // código postal
            "Ciudad de México", // municipio
            "CDMX", // estado
            "juan.perez@example.com", // email
            "555-1234567", // teléfono
            "password123", // contraseña (ejemplo)
            null // fotoUsuario (aquí cargarías la imagen del usuario si está disponible)
        )
    }

    companion object {
        private const val REQUEST_IMAGE_GALLERY = 1
    }
}

package com.example.login_page_project

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProductUpdate : AppCompatActivity() {

    private lateinit var editTextMarca: EditText
    private lateinit var editTextNombre: EditText
    private lateinit var editTextDescripcion: EditText
    private lateinit var editTextPrecio: EditText
    private lateinit var editTextCantidad: EditText
    private lateinit var imageViewProducto: ImageView
    private lateinit var buttonUploadImagen: Button
    private lateinit var buttonActualizarProducto: Button
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private var idproducto: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_update)

        editTextMarca = findViewById(R.id.editTextMarca)
        editTextNombre = findViewById(R.id.editTextNombreProducto)
        editTextDescripcion = findViewById(R.id.editTextDescripcion)
        editTextPrecio = findViewById(R.id.editTextPrecio)
        editTextCantidad = findViewById(R.id.editTextCantidad)
        imageViewProducto = findViewById(R.id.imageViewProducto)
        buttonUploadImagen = findViewById(R.id.buttonUploadImagen)
        buttonActualizarProducto = findViewById(R.id.buttonActualizarProducto)

        // Recibir los datos del producto a actualizar
        idproducto = intent.getIntExtra("idproducto", 0)
        editTextMarca.setText(intent.getStringExtra("marca"))
        editTextNombre.setText(intent.getStringExtra("nombre"))
        editTextDescripcion.setText(intent.getStringExtra("descripcion"))
        editTextPrecio.setText(intent.getDoubleExtra("precio", 0.0).toString())
        editTextCantidad.setText(intent.getIntExtra("cantidad", 0).toString())
        val urlImagen = intent.getStringExtra("urlimagen")

        // Cargar la imagen del producto
        urlImagen?.let { Glide.with(this).load(it).into(imageViewProducto) }

        buttonUploadImagen.setOnClickListener {
            openImageChooser()
        }

        buttonActualizarProducto.setOnClickListener {
            updateProduct()
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageViewProducto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateProduct() {
        val marca = editTextMarca.text.toString().trim()
        val nombre = editTextNombre.text.toString().trim()
        val descripcion = editTextDescripcion.text.toString().trim()
        val precio = editTextPrecio.text.toString().toDoubleOrNull() ?: 0.0
        val cantidad = editTextCantidad.text.toString().toIntOrNull() ?: 0
        /*val idusuario = Login.UserManager.userId ?: return*/

        if (marca.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || precio <= 0 || cantidad <= 0) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.35:8080/login/") // Reemplaza con la URL de tu servicio web
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val service = retrofit.create(ProductService::class.java)

        val idBody = idproducto.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val marcaBody = marca.toRequestBody("text/plain".toMediaTypeOrNull())
        val nombreBody = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
        val descripcionBody = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
        val precioBody = precio.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val cantidadBody = cantidad.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        /*val usuarioBody = idusuario.toString().toRequestBody("text/plain".toMediaTypeOrNull())*/

        val multipartBody: MultipartBody.Part? = imageUri?.let {
            val bitmap = (imageViewProducto.drawable as BitmapDrawable).bitmap
            val file = File(cacheDir, "imagen.jpg")
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
            }

            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("urlimagen", file.name, requestBody)
        }

        val call = service.updateProduct(
            idBody,
            marcaBody,
            nombreBody,
            descripcionBody,
            precioBody,
            cantidadBody,
            /*usuarioBody,*/
            multipartBody
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductUpdate, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ProductUpdate, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ProductUpdate, "Error al actualizar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ProductUpdate, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
package com.example.login_page_project

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login_page_project.Home.Utils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RegisterProductActivity : AppCompatActivity() {

    private lateinit var editTextMarca: EditText
    private lateinit var editTextNombre: EditText
    private lateinit var editTextDescripcion: EditText
    private lateinit var editTextPrecio: EditText
    private lateinit var editTextCantidad: EditText
    private lateinit var imageViewProducto: ImageView
    private lateinit var buttonUploadImagen: Button
    private lateinit var buttonRegistrarProducto: Button
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var btnMenu: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_product)
        btnMenu = findViewById(R.id.btn_menu)
        val someView: View = findViewById(R.id.btn_menu)
        someView.setOnClickListener {
            Utils.showPopupMenu(this, it)
        }

        editTextMarca = findViewById(R.id.editTextMarca)
        editTextNombre = findViewById(R.id.editTextNombreProducto)
        editTextDescripcion = findViewById(R.id.editTextDescripcion)
        editTextPrecio = findViewById(R.id.editTextPrecio)
        editTextCantidad = findViewById(R.id.editTextCantidad)
        imageViewProducto = findViewById(R.id.imageViewProducto)
        buttonUploadImagen = findViewById(R.id.buttonUploadImagen)
        buttonRegistrarProducto = findViewById(R.id.buttonRegistrarProducto)

        buttonUploadImagen.setOnClickListener {
            openImageChooser()
        }

        buttonRegistrarProducto.setOnClickListener {
            insertProduct()
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

    private fun insertProduct() {
        val marca = editTextMarca.text.toString().trim()
        val nombre = editTextNombre.text.toString().trim()
        val descripcion = editTextDescripcion.text.toString().trim()
        val precio = editTextPrecio.text.toString().toDouble()
        val cantidad = editTextCantidad.text.toString().toInt()

        if (marca.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || precio <= 0 || cantidad <= 0) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Configurar Retrofit y el servicio
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.35:8080/login/") // Reemplaza con la URL de tu servicio web
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val service = retrofit.create(ProductService::class.java)

        // Realizar la solicitud para registrar el producto sin imagen
        val call = service.insertProduct(
            marca.toRequestBody(),
            nombre.toRequestBody(),
            descripcion.toRequestBody(),
            precio.toString().toRequestBody(),
            cantidad.toString().toRequestBody()
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val idproducto = JSONObject(responseBody).getInt("idproducto")
                    uploadImage(idproducto)
                } else {
                    Toast.makeText(this@RegisterProductActivity, "Error al registrar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RegisterProductActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadImage(idproducto: Int) {
        // Convertir imagen a archivo
        val bitmap = (imageViewProducto.drawable as BitmapDrawable).bitmap
        val file = File(cacheDir, "imagen.jpg")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val multipartBody = MultipartBody.Part.createFormData("urlimagen", file.name, requestBody)
        val idBody = idproducto.toString().toRequestBody()

        // Configurar Retrofit y el servicio
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.35:8080/login/") // Reemplaza con la URL de tu servicio web
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val service = retrofit.create(ProductService::class.java)

        // Realizar la solicitud para subir la imagen y actualizar la ruta en la base de datos
        val call = service.uploadImage(idBody, multipartBody)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterProductActivity, "Producto registrado con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent (this@RegisterProductActivity, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RegisterProductActivity, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RegisterProductActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Extensión para convertir String en RequestBody
    private fun String.toRequestBody(): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), this)
    }
}
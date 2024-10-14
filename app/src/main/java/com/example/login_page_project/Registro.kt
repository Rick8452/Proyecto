package com.example.login_page_project

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Registro() : AppCompatActivity(), Parcelable {
    private lateinit var t_nombre: EditText
    private lateinit var t_apellidos: EditText
    private lateinit var t_email: EditText
    private lateinit var t_telefono: EditText
    private lateinit var t_password: EditText
    private lateinit var t_confirmarcontraseña: EditText
    private lateinit var t_edad: EditText
    private lateinit var t_direccion: EditText
    private lateinit var t_codigo_postal: EditText
    private lateinit var t_municipio: EditText
    private lateinit var t_estado: EditText
    lateinit var log: TextView
    private lateinit var b_insertar: Button
    private lateinit var requestQueue: RequestQueue

    constructor(parcel: Parcel) : this() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        t_nombre = findViewById(R.id.txtnombre)
        t_apellidos = findViewById(R.id.txtapellidos)
        t_email = findViewById(R.id.txtemail)
        t_telefono = findViewById(R.id.txttelefono)
        t_password = findViewById(R.id.txtpassword)
        t_confirmarcontraseña = findViewById(R.id.txtconfirmarcontraseña)
        t_edad = findViewById(R.id.txtedad)
        t_direccion = findViewById(R.id.txtdireccion)
        t_codigo_postal = findViewById(R.id.txtcodigo_postal)
        t_municipio = findViewById(R.id.txtmunicipio)
        t_estado = findViewById(R.id.txtestado)
        b_insertar = findViewById(R.id.btninsertar)
        requestQueue = Volley.newRequestQueue(this)

        b_insertar.setOnClickListener {
            insertarDatos()
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Registro> {
        override fun createFromParcel(parcel: Parcel): Registro {
            return Registro(parcel)
        }

        override fun newArray(size: Int): Array<Registro?> {
            return arrayOfNulls(size)
        }
    }

    private fun mostrarAlertDialog(mensaje: String) {
        val builder = AlertDialog.Builder(this@Registro)
        builder.setTitle("Mensaje")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    private fun insertarDatos() {
        val nombre = t_nombre.text.toString().trim()
        val apellidos = t_apellidos.text.toString().trim()
        val email = t_email.text.toString().trim()
        val telefono = t_telefono.text.toString().trim()
        val password = t_password.text.toString().trim()
        val confirmPassword = t_confirmarcontraseña.text.toString().trim()
        val edad = t_edad.text.toString().trim()
        val direccion = t_direccion.text.toString().trim()
        val codigoPostal = t_codigo_postal.text.toString().trim()
        val municipio = t_municipio.text.toString().trim()
        val estado = t_estado.text.toString().trim()

        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || telefono.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            edad.isEmpty() || direccion.isEmpty() || codigoPostal.isEmpty() || municipio.isEmpty() || estado.isEmpty()) {
            Toast.makeText(applicationContext, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (nombre.any { it.isDigit() } || apellidos.any { it.isDigit() }) {
            mostrarAlertDialog("El nombre y apellidos no pueden contener números")
            return
        }

        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!email.matches(emailRegex)) {
            mostrarAlertDialog("El formato del email no es válido")
            return
        }

        if (telefono.length != 10 || telefono.any { !it.isDigit() }) {
            mostrarAlertDialog("El teléfono debe contener 10 dígitos y ningún otro carácter")
            return
        }

        if (password.length < 6) {
            mostrarAlertDialog("La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (password != confirmPassword) {
            mostrarAlertDialog("Las contraseñas no coinciden")
            return
        }

        val progressDialog = ProgressDialog(this@Registro)
        progressDialog.setMessage("Insertando datos...")
        progressDialog.show()

        val url = "http://192.168.100.35:8080/login/insertar.php"

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Registro, Login::class.java)
                startActivity(intent)
                finish()
            },
            Response.ErrorListener { error ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["nombre"] = nombre
                params["apellidos"] = apellidos
                params["email"] = email
                params["telefono"] = telefono
                params["password"] = password
                params["edad"] = edad
                params["direccion"] = direccion
                params["codigo_postal"] = codigoPostal
                params["municipio"] = municipio
                params["estado"] = estado
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
}
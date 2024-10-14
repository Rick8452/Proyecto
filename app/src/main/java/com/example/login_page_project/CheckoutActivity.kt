import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login_page_project.CarritoActivity
import com.example.login_page_project.Home
import com.example.login_page_project.Home.Utils
import com.example.login_page_project.R

class CheckoutActivity : AppCompatActivity() {
    private lateinit var telefonoEditText: EditText
    private lateinit var confirmOrderButton: Button
    private lateinit var requestQueue: RequestQueue
    private lateinit var btnMenu: ImageButton
    private lateinit var btnCarrito: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        telefonoEditText = findViewById(R.id.telefonoEditText)
        confirmOrderButton = findViewById(R.id.confirmOrderButton)
        requestQueue = Volley.newRequestQueue(this)
        confirmOrderButton.setOnClickListener {
            processOrder()
        }

        btnMenu = findViewById(R.id.btn_menu)
        btnMenu.setOnClickListener {
            Utils.showPopupMenu(this, it)
        }

        // Inicializar el botón de carrito
        btnCarrito = findViewById(R.id.carrito)
        btnCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }
    }

    private fun processOrder() {
        val telefono = telefonoEditText.text.toString()
        val url = "http://192.168.100.35:8080/login/process_order.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this, "Su órden se ha completado con exito", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@CheckoutActivity, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            },
            Response.ErrorListener {
                Toast.makeText(this, "El proceso de su orden falló", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["idusuario"] = "1"
                params["telefono"] = telefono
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
}
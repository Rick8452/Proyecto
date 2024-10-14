package com.example.login_page_project

import CheckoutActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.login_page_project.Home.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CarritoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var proceedToCheckoutButton: Button
    private lateinit var requestQueue: RequestQueue
    private lateinit var btnMenu: ImageButton
    private lateinit var btnCarrito: ImageButton

    private var cartList = mutableListOf<CartItem>()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.recycler_view_cart)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        proceedToCheckoutButton = findViewById(R.id.proceedToCheckoutButton)
        requestQueue = Volley.newRequestQueue(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(cartList)
        recyclerView.adapter = adapter

        loadCartItems()

        proceedToCheckoutButton.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
        btnMenu = findViewById(R.id.btn_menu)
        val someView: View = findViewById(R.id.btn_menu)
        someView.setOnClickListener {
            Utils.showPopupMenu(this, it)
        }

        // Inicializar el botón de carrito
        btnCarrito = findViewById(R.id.carrito)
        btnCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }
    }

    private fun loadCartItems() {
        val url = "http://192.168.100.35:8080/login/get_cart.php?idusuario=1"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val type = object : TypeToken<List<CartItem>>() {}.type
                cartList = Gson().fromJson(response.toString(), type)
                adapter.updateData(cartList)
                updateTotalPrice()
            },
            Response.ErrorListener {
                Toast.makeText(this, "Error loading cart items", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun updateTotalPrice() {
        var totalPrice = 0.0
        for (item in cartList) {
            if (item.precio != null && item.cantidad != null) {
                totalPrice += item.precio * item.cantidad
            }
        }
        totalPriceTextView.text = "Total: $totalPrice"
    }
}
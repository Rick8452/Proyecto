package com.example.login_page_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Home : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ProductAdapter
    private lateinit var btnMenu: ImageButton
    private lateinit var btnCarrito: ImageButton
    private lateinit var productList: MutableList<Producto>
    private val productService: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.100.35:8080/login/") // Reemplaza con la URL de tu servicio web
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ProductService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Login.UserManager.init(this)
        setContentView(R.layout.home)

        // Verificar si el usuario está autenticado
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (!isLoggedIn) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Inicializar el RecyclerView y el adaptador
        recyclerView = findViewById(R.id.recycler_view)
        searchView = findViewById(R.id.searchView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productList = mutableListOf()
        adapter = ProductAdapter(productList) { producto ->
            // Manejo del clic en el botón de edición
            openEditProductActivity(producto)
        }
        recyclerView.adapter = adapter

        // Inicializar el botón de menú
        btnMenu = findViewById(R.id.btn_menu)
        btnMenu.setOnClickListener {
            Utils.showPopupMenu(this, it)
        }

        // Inicializar el botón de carrito
        btnCarrito = findViewById(R.id.carrito)
        btnCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        // Configurar el SearchView utilizando la clase utilitaria
        SearchViewUtil.setupSearchView(searchView, this, productService) { productos ->
            adapter.updateData(productos)
        }

        // Llamar método para obtener datos del servicio web
        fetchDataFromWebService()
    }

    private fun openEditProductActivity(producto: Producto) {
        val intent = Intent(this, ProductUpdate::class.java).apply {
            putExtra("idproducto", producto.idProducto)
            putExtra("marca", producto.marca)
            putExtra("nombre", producto.nombre)
            putExtra("descripcion", producto.descripcion)
            putExtra("precio", producto.precio)
            putExtra("cantidad", producto.cantidad)
            putExtra("urlimagen", producto.urlimagen)
        }
        startActivity(intent)
    }

    object Utils {
        fun showPopupMenu(context: Context, view: View) {
            val popupMenu = PopupMenu(context, view)
            popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    /*R.id.action_categories -> {
                        Toast.makeText(context, "Categorías seleccionadas", Toast.LENGTH_SHORT).show()
                        true
                    }*/
                    R.id.action_account -> {
                        Toast.makeText(context, "Mi cuenta seleccionada", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_help -> {
                        Toast.makeText(context, "Ayuda seleccionada", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_home -> {
                        // Abrir la actividad de registro de tienda
                        context.startActivity(Intent(context, Home::class.java))
                        true
                    }
                    R.id.action_productos -> {
                        // Abrir la actividad de productos
                        context.startActivity(Intent(context, RegisterProductActivity::class.java))
                        true
                    }
                    R.id.action_logout -> {
                        // Cerrar sesión
                        if (context is Home) {
                            context.logout()
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun fetchDataFromWebService() {
        lifecycleScope.launch {
            try {
                val call = productService.getProductos()
                call.enqueue(object : retrofit2.Callback<List<Producto>> {
                    override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                        if (response.isSuccessful) {
                            val productos = response.body() ?: emptyList()
                            productList.clear()
                            productList.addAll(productos)
                            adapter.updateData(productList)
                        } else {
                            Toast.makeText(this@Home, "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                        Toast.makeText(this@Home, "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(this@Home, "Error al obtener los productos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

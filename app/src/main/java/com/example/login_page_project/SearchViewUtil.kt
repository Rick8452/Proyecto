package com.example.login_page_project

import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SearchViewUtil {

    fun setupSearchView(searchView: SearchView, context: Context, productService: ProductService, onProductsFound: (List<Producto>) -> Unit) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchProducts(query, context, productService, onProductsFound)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    searchProducts(newText, context, productService, onProductsFound)
                }
                return false
            }
        })
        searchView.isIconified = false
        searchView.requestFocus()
    }

    private fun searchProducts(query: String, context: Context, productService: ProductService, onProductsFound: (List<Producto>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val productos = withContext(Dispatchers.IO) {
                    productService.searchProducts(query).execute().body() ?: emptyList()
                }
                onProductsFound(productos)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al realizar la búsqueda", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

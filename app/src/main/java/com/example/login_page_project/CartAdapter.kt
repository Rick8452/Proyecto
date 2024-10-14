package com.example.login_page_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartAdapter(private var cartList: List<CartItem>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    fun updateData(newList: List<CartItem>) {
        cartList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageProducto: ImageView = itemView.findViewById(R.id.imageProducto)
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textDescripcion)
        private val priceTextView: TextView = itemView.findViewById(R.id.textPrecio)
        private val cantidadTextView: TextView = itemView.findViewById(R.id.textCantidad)
        private val removeFromCartButton: Button = itemView.findViewById(R.id.removeFromCartButton)

        fun bind(cartItem: CartItem) {
            // Cargar la imagen utilizando Glide
            val imageUrl = "http://192.168.100.35:8080/login/${cartItem.urlimagen}"

            Glide.with(itemView.context)
                .load(imageUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder_image) // Imagen de placeholder opcional mientras se carga la imagen
                        .error(R.drawable.error_image) // Imagen de error opcional si falla la carga de la imagen
                )
                .into(imageProducto)

            textName.text = cartItem.nombre
            descriptionTextView.text = cartItem.descripcion
            priceTextView.text = "$ ${cartItem.precio}"
            cantidadTextView.text = "Cantidad: ${cartItem.cantidad}"

            removeFromCartButton.setOnClickListener {
                Toast.makeText(itemView.context, "${cartItem.nombre} eliminado del carrito", Toast.LENGTH_SHORT).show()
                cartItem.idProducto?.let { it1 -> removeFromCart(it1) }
            }
        }

        private fun removeFromCart(idproducto: Int) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.100.35:8080/login/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ProductService::class.java)
            val call = service.removeFromCart(idproducto, 1) // Cambia el 1 por el id del usuario

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Actualiza la lista después de eliminar el producto
                        cartList = cartList.filter { it.idProducto != idproducto }
                        notifyDataSetChanged()
                    } else {
                        Toast.makeText(itemView.context, "Error al eliminar el producto del carrito", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(itemView.context, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

package com.example.login_page_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProductAdapter(private var productList: List<Producto>,
                     private val onEditClick: (Producto) -> Unit) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productList[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateData(newList: List<Producto>) {
        productList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageProducto: ImageView = itemView.findViewById(R.id.imageProducto)
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textDescripcion)
        private val priceTextView: TextView = itemView.findViewById(R.id.textPrecio)
        private val textMarca: TextView = itemView.findViewById(R.id.textMarca)
        private val textCantidad: TextView = itemView.findViewById(R.id.textCantidad)
        private val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)
        private val buttonEditProduct: Button = itemView.findViewById(R.id.buttonEditProduct)

        fun bind(producto: Producto) {
            // Cargar la imagen utilizando Glide
            val imageUrl = "http://192.168.100.35:8080/login/${producto.urlimagen}"

            Glide.with(itemView.context)
                .load(imageUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder_image) // Imagen de placeholder opcional mientras se carga la imagen
                        .error(R.drawable.error_image) // Imagen de error opcional si falla la carga de la imagen
                )
                .into(imageProducto)

            textName.text = producto.nombre
            descriptionTextView.text = producto.descripcion
            priceTextView.text = "$ ${producto.precio}"
            textMarca.text = "Marca: ${producto.marca}"
            textCantidad.text = "Cantidad: ${producto.cantidad}"

            addToCartButton.setOnClickListener {
                addToCart(producto)
            }
            buttonEditProduct.setOnClickListener {
                onEditClick(producto)
            }

        }


        private fun addToCart(producto: Producto) {
            val url = "http://192.168.100.35:8080/login/add_to_cart.php"
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Toast.makeText(itemView.context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(itemView.context, "Error al agregar al carrito", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["idusuario"] = "1"
                    params["idproducto"] = producto.idProducto.toString()
                    params["precio"] = producto.precio.toString()
                    return params
                }
            }
            Volley.newRequestQueue(itemView.context).add(stringRequest)
        }
    }
}
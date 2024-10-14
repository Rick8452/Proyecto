package com.example.login_page_project

data class CartItem(
    val idProducto: Int?,
    val id: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null,
    val precio: Double? = null,
    val cantidad: Int? = null,
    val urlimagen: String? = null
)
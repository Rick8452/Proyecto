package com.example.login_page_project

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("idproducto") val idProducto: Int?,
    @SerializedName("marca") val marca: String,
    @SerializedName("name") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("urlimagen") val urlimagen: String
)
package com.example.login_page_project

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("idusuario") val idUsuario: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("codigo_postal") val codigoPostal: String,
    @SerializedName("municipio") val municipio: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("email") val email: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("password") val password: String,
    @SerializedName("foto_usuario") val fotoUsuario: String? // Assumiendo que foto_usuario es un Bitmap
)


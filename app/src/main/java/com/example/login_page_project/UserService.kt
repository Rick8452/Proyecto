package com.example.login_page_project

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {

    @GET("get_user.php")
    fun getUserData(@Query("idusuario") idUsuario: Int): Call<Usuario>

    @GET("get_user_image.php")
    fun getUserImage(@Query("idusuario") idUsuario: Int): Call<ResponseBody> // Cambiar a Bitmap si manejas la imagen como Bitmap

    @Multipart
    @POST("update_user.php") // Cambia el nombre del archivo PHP según tu implementación
    fun updateUser(
        @Part("idusuario") idUsuario: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("apellidos") apellidos: RequestBody,
        @Part("edad") edad: RequestBody,
        @Part("direccion") direccion: RequestBody,
        @Part("codigo_postal") codigoPostal: RequestBody,
        @Part("municipio") municipio: RequestBody,
        @Part("estado") estado: RequestBody,
        @Part("email") email: RequestBody,
        @Part("telefono") telefono: RequestBody,
        @Part("password") password: RequestBody,
        @Part fotoUsuario: MultipartBody.Part? // Para subir la imagen del usuario
    ): Call<ResponseBody>
}


package com.example.login_page_project

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


object ApiClient {
    private const val BASE_URL = "http://192.168.100.35:8080/login/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    interface ApiServices {
        @Multipart
        @POST("upload_image.php")
        fun uploadImage(@Part image: MultipartBody.Part): Call<ResponseBody>

        @POST("create_product.php")
        fun addProduct(
            @Part("marca") marca: String,
            @Part("name") name: String,
            @Part("descripcion") descripcion: String,
            @Part("color") color: String,
            @Part("precio") precio: Double,
            @Part("cantidad") cantidad: Int,
            @Part("idcategoria") idcategoria: Int,
            @Part("urlimagen") urlimagen: String
        ): Call<ResponseBody>
        @DELETE("productos.php")
        fun deleteProduct(@Path("id") id: Int): Call<ResponseBody>
    }
}



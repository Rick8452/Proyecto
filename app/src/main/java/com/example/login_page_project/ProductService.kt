package com.example.login_page_project

import com.android.billingclient.api.ProductDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ProductService {

    // Define la ruta relativa desde la URL base del servidor

    @GET("productos.php")
    fun getProductos(): Call<List<Producto>>
    @GET("search_products.php")
    fun searchProducts(@Query("query") query: String): Call<List<Producto>>


    @Multipart
    @POST("insertar_producto.php") // Ajusta el nombre del archivo PHP según tu implementación
    fun insertProduct(
        @Part("marca") marca: RequestBody,
        @Part("name") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("cantidad") cantidad: RequestBody,
        /*@Part("idusuario") idusuario: RequestBody,*/

    ): Call<ResponseBody>
    @Multipart
    @POST("upload_image.php")
    fun uploadImage(
        @Part("idproducto") idproducto: RequestBody,

        @Part urlimagen: MultipartBody.Part
    ): Call<ResponseBody>
    @POST("remove_from_cart.php")
    @FormUrlEncoded
    fun removeFromCart(@Field("idproducto") idproducto: Int, @Field("idusuario") idusuario: Int): Call<ResponseBody>

    @Multipart
    @POST("update_product.php")
    fun updateProduct(
        @Part("idproducto") idproducto: RequestBody,
        @Part("marca") marca: RequestBody,
        @Part("name") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("cantidad") cantidad: RequestBody,
        @Part urlimagen: MultipartBody.Part? // Cambiado para usar @Part para la imagen
    ): Call<ResponseBody>
    @GET("get_products.php")
    fun getProductDetails(@Query("idproducto") idproducto: Int): Call<ProductDetails>



}

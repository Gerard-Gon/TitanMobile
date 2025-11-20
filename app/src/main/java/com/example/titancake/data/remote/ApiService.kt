package com.example.titancake.data.remote

import com.example.titancake.data.model.Producto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// Esta interfaz define los endpoints HTTP
interface ApiService {

    // Define una solicitud GET al endpoint /posts
    @GET("api/v1/productos")
    suspend fun getProductos(): List<Producto>

    @GET("api/v1/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Int): Producto

    @PUT("api/v1/productos/{id}")
    suspend fun updateProducto(
        @Path("id") id: Int,
        @Body producto: Producto
    ): Response<Producto>

    @DELETE("api/v1/productos/{id}")
    suspend fun deleteProducto(
        @Path("id") id: Int
    ): Response<Unit>


}

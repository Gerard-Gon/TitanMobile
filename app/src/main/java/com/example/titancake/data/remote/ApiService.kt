package com.example.titancake.data.remote

import com.example.titancake.data.model.Producto
import retrofit2.http.GET

// Esta interfaz define los endpoints HTTP
interface ApiService {

    // Define una solicitud GET al endpoint /posts
    @GET("/productos")
    suspend fun getProductos(): List<Producto>

}

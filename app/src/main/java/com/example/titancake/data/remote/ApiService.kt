package com.example.titancake.data.remote

import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import com.example.titancake.data.model.Usuario
import com.example.titancake.data.model.UsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Esta interfaz define los endpoints HTTP
interface ApiService {

    // CRUD PRODUCTOS
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

    @POST("api/v1/productos")
    suspend fun addProducto(
        @Body producto: ProductoRequest
    ): Response<Producto>

    // CRUD USUARIOS

    @GET("api/v1/usuarios")
    suspend fun getUsuarios(): List<Usuario>

    @GET("api/v1/usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): Usuario

    @PUT("api/v1/usuarios/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Int,
        @Body usuario: Usuario
    ): Response<Usuario>

    @DELETE("api/v1/usuarios/{id}")
    suspend fun deleteUsuario(
        @Path("id") id: Int
    ): Response<Unit>

    @POST("api/v1/usuarios")
    suspend fun addUsuario(
        @Body usuario: UsuarioRequest
    ): Response<Usuario>

    //CRUD CARRITO

}

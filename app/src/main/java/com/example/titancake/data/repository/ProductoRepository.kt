package com.example.titancake.data.repository
import com.example.titancake.data.model.Producto
import com.example.titancake.data.remote.RetrofitInstance
import com.example.titancake.ui.components.pasteles

class ProductoRepository {

    // Esta lista contiene todos los productos disponibles en TitanCake.
    private val productos = pasteles

    // Esta función devuelve todos los productos.
    fun getAll(): List<Producto> = productos

    // Esta función busca un producto específico por su ID.
    fun getById(id: Int): Producto? = productos.find { it.id == id }

    suspend fun getProductos(): List<Producto> {
        return RetrofitInstance.api.getProductos()
    }

}
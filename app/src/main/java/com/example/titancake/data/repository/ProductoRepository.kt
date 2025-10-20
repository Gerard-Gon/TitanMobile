package com.example.titancake.data.repository
import com.example.titancake.data.model.Producto
import com.example.titancake.ui.components.pasteles

class ProductoRepository {

    private val productos = pasteles
    fun getAll(): List<Producto> = productos
    fun getById(id: Int): Producto? = productos.find { it.id == id }

}
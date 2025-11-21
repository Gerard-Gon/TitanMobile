package com.example.titancake.data.repository
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import com.example.titancake.data.remote.RetrofitInstance
import com.example.titancake.data.model.pasteles
import retrofit2.Response


interface ProductoRepositoryInterface {
    suspend fun getProductos(): List<Producto>
}

class ProductoRepository : ProductoRepositoryInterface{

    // Esta lista contiene todos los productos disponibles en TitanCake.
    private val productos = pasteles

    // Esta función devuelve todos los productos.
    fun getAll(): List<Producto> = productos

    // Esta función busca un producto específico por su ID.
    fun getById(id: Int): Producto? = productos.find { it.id == id }

    override suspend fun getProductos(): List<Producto> {
        return RetrofitInstance.api.getProductos()
    }

    suspend fun addProducto(producto: ProductoRequest): Response<Producto> {
        return RetrofitInstance.api.addProducto(producto)
    }

    suspend fun deleteProducto(id: Int): Response<Unit> {
        return RetrofitInstance.api.deleteProducto(id)
    }

    suspend fun updateProducto(id: Int, producto: Producto): Response<Producto> {
        return RetrofitInstance.api.updateProducto(id, producto)
    }

    /*
    override suspend fun actualizarProducto(id: Int, body: Producto): Producto {
        return RetrofitInstance.api.updateProducto(id=id, producto=body)
    }
    */


}
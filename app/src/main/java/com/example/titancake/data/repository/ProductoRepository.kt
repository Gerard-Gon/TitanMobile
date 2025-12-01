package com.example.titancake.data.repository
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import com.example.titancake.data.remote.RetrofitInstance
import retrofit2.Response


interface ProductoRepositoryInterface {
    suspend fun getProductos(): List<Producto>
    suspend fun addProducto(producto: ProductoRequest): Response<Producto>
    suspend fun deleteProducto(id: Int): Response<Unit>
    suspend fun updateProducto(id: Int, producto: Producto): Response<Producto>
}

class ProductoRepository : ProductoRepositoryInterface{

    override suspend fun getProductos(): List<Producto> {
        return RetrofitInstance.api.getProductos()
    }

    override suspend fun addProducto(producto: ProductoRequest): Response<Producto> {
        return RetrofitInstance.api.addProducto(producto)
    }

    override suspend fun deleteProducto(id: Int): Response<Unit> {
        return RetrofitInstance.api.deleteProducto(id)
    }

    override suspend fun updateProducto(id: Int, producto: Producto): Response<Producto> {
        return RetrofitInstance.api.updateProducto(id, producto)
    }

    /*
    override suspend fun actualizarProducto(id: Int, body: Producto): Producto {
        return RetrofitInstance.api.updateProducto(id=id, producto=body)
    }
    */


}
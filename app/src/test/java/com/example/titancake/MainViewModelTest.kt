package com.example.titancake

import com.example.titancake.data.model.Producto
import com.example.titancake.data.repository.ProductoRepositoryInterface
import com.example.titancake.ui.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import com.example.titancake.data.model.ProductoRequest
import retrofit2.Response

class FakeProductoRepository : ProductoRepositoryInterface {

    private val productos = mutableListOf(
        Producto(id = 1, nombreProducto = "Prueba1", precio = 100000, descripcionProducto = "Descripción1", stock = 10, imageUrl = ""),
        Producto(id = 2, nombreProducto = "Prueba2", precio = 100000, descripcionProducto = "Descripción2", stock = 10, imageUrl = "")
    )

    override suspend fun getProductos(): List<Producto> {
        return productos
    }

    override suspend fun addProducto(producto: ProductoRequest): Response<Producto> {
        val nuevo = Producto(
            id = productos.size + 1,
            nombreProducto = producto.nombreProducto,
            precio = producto.precio,
            descripcionProducto = producto.descripcionProducto,
            stock = producto.stock,
            imageUrl = producto.imageUrl
        )
        productos.add(nuevo)
        return Response.success(nuevo)
    }

    override suspend fun deleteProducto(id: Int): Response<Unit> {
        val eliminado = productos.removeIf { it.id == id }
        return if (eliminado) Response.success(Unit) else Response.error(404, okhttp3.ResponseBody.create(null, ""))
    }

    override suspend fun updateProducto(id: Int, producto: Producto): Response<Producto> {
        val index = productos.indexOfFirst { it.id == id }
        return if (index != -1) {
            productos[index] = producto
            Response.success(producto)
        } else {
            Response.error(404, okhttp3.ResponseBody.create(null, ""))
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @Test
    fun `cuando se inicializa el ViewModel carga los productos correctamente`() = runTest {
        // GIVEN: un repositorio falso con datos estáticos
        val fakeRepository = FakeProductoRepository()

        // WHEN: se crea el ViewModel con el repositorio falso
        val viewModel = MainViewModel(fakeRepository)

        // THEN: el flujo productosList contiene los datos esperados
        val productos = viewModel.productosList.first()

        assertEquals(2, productos.size)
        assertEquals("Prueba1", productos.first().nombreProducto)
    }
}
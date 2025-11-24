package com.example.titancake

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.titancake.ui.screens.HomeScreen
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.MainViewModel
import com.example.titancake.data.repository.ProductoRepositoryInterface
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import org.junit.Rule
import androidx.navigation.compose.rememberNavController
import com.example.titancake.data.model.Categoria
import org.junit.Test
import retrofit2.Response

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Repositorio falso para pruebas
    private val fakeRepository = object : ProductoRepositoryInterface {
        override suspend fun getProductos(): List<Producto> = listOf(
            Producto(1, "Pastel de Chocolate", 5000, "Delicioso pastel con cobertura", 10, "", Categoria(1, "Pastel")),
            Producto(2, "Tarta de Frutilla", 4500, "Tarta fresca con frutillas naturales", 8, "", Categoria(2, "Dulce"))
        )

        override suspend fun addProducto(producto: ProductoRequest): Response<Producto> =
            Response.success(
                Producto(
                    3,
                    producto.nombreProducto,
                    producto.precio,
                    producto.descripcionProducto,
                    producto.stock,
                    producto.imageUrl,
                    Categoria(producto.categoria.id, "Categoría Test")
                )
            )

        override suspend fun deleteProducto(id: Int): Response<Unit> = Response.success(Unit)

        override suspend fun updateProducto(id: Int, producto: Producto): Response<Producto> =
            Response.success(producto)
    }

    @Test
    fun muestraElTituloPrincipal() {
        composeTestRule.setContent {
            HomeScreen(
                viewModel = MainViewModel(fakeRepository),
                cartViewModel = CartViewModel(),
                onItemClick = {},
                onClick = {},
                navController = rememberNavController()
            )
        }

        // Verifica que el título principal esté visible
        composeTestRule.onNodeWithText("TITANCAKE MOBILE").assertIsDisplayed()
    }

    @Test
    fun muestraLosProductosEnPantalla() {
        composeTestRule.setContent {
            HomeScreen(
                viewModel = MainViewModel(fakeRepository),
                cartViewModel = CartViewModel(),
                onItemClick = {},
                onClick = {},
                navController = rememberNavController()
            )
        }

        // Verifica que los nombres de los productos estén visibles
        composeTestRule.onNodeWithText("Pastel de Chocolate").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tarta de Frutilla").assertIsDisplayed()

        // Verifica que al menos un botón "Agregar al carrito" esté visible
        composeTestRule.onAllNodesWithText("Agregar al carrito")[0].assertIsDisplayed()
    }
}
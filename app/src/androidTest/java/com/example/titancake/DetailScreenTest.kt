package com.example.titancake

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.titancake.data.model.Categoria
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import com.example.titancake.data.repository.ProductoRepositoryInterface
import com.example.titancake.ui.screens.DetailScreen
import com.example.titancake.ui.viewmodel.MainViewModel
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeRepository = object : ProductoRepositoryInterface {
        override suspend fun getProductos(): List<Producto> = listOf() // No se usa aquí

        override suspend fun addProducto(producto: ProductoRequest): Response<Producto> = Response.success(null)
        override suspend fun deleteProducto(id: Int): Response<Unit> = Response.success(Unit)
        override suspend fun updateProducto(id: Int, producto: Producto): Response<Producto> = Response.success(producto)
    }

    @Test
    fun detailScreen_MuestraInformacionDelProducto() {

        val viewModel = MainViewModel(fakeRepository)


        composeTestRule.setContent {
            DetailScreen(
                itemId = 1,
                viewModel = viewModel,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Detalle Producto").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Atrás").assertIsDisplayed()
    }
}
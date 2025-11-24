package com.example.titancake

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import com.example.titancake.data.repository.AuthRepository
import com.example.titancake.data.repository.ProductoRepositoryInterface
import com.example.titancake.ui.screens.admin.AdminScreen
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.viewmodel.MainViewModel
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class AdminScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeRepository = object : ProductoRepositoryInterface {
        override suspend fun getProductos(): List<Producto> = emptyList()
        override suspend fun addProducto(producto: ProductoRequest): Response<Producto> = Response.success(null)
        override suspend fun deleteProducto(id: Int): Response<Unit> = Response.success(Unit)
        override suspend fun updateProducto(id: Int, producto: Producto): Response<Producto> = Response.success(producto)
    }

    @Test
    fun adminScreen_MuestraCamposDeEntrada() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val authRepository = AuthRepository(context)
        val authViewModel = AuthViewModel(authRepository)

        val mainViewModel = MainViewModel(fakeRepository)

        composeTestRule.setContent {
            AdminScreen(
                authViewModel = authViewModel,
                navControllerApp = rememberNavController(),
                viewModel = mainViewModel
            )
        }


        composeTestRule.onNode(
            hasText("Agregar Producto") and !hasClickAction()
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText("Nombre del producto").assertIsDisplayed()
        composeTestRule.onNodeWithText("Precio").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stock").assertIsDisplayed()
        composeTestRule.onNodeWithText("ID de Categoría (Obligatorio)").assertIsDisplayed()


        composeTestRule.onNode(
            hasText("Agregar Producto") and hasClickAction()
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText("Nombre del producto").performTextInput("Pastel de Prueba")
    }
}
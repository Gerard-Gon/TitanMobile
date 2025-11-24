package com.example.titancake

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.titancake.data.model.Categoria
import com.example.titancake.data.model.Producto
import com.example.titancake.ui.screens.ShoppingCartScreen
import com.example.titancake.ui.viewmodel.CartViewModel
import org.junit.Rule
import org.junit.Test

class ShoppingCartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shoppingCart_MuestraProductosYTotalCorrecto() {
        val cartViewModel = CartViewModel()

        val productoPrueba = Producto(
            id = 1,
            nombreProducto = "Pastel Test",
            precio = 1000,
            descripcionProducto = "Desc",
            stock = 10,
            imageUrl = "",
            categoria = Categoria(1, "Test")
        )

        cartViewModel.agregarProducto(productoPrueba, 2)

        composeTestRule.setContent {
            ShoppingCartScreen(
                cartViewModel = cartViewModel,
                onConfirm = {}
            )
        }


        composeTestRule.onNodeWithText("Pastel Test").assertIsDisplayed()

        composeTestRule.onNodeWithText("Cantidad: 2").assertIsDisplayed()

        composeTestRule.onNodeWithText("Subtotal: $2000").assertIsDisplayed()

        composeTestRule.onNodeWithText("Total a Pagar: $2000").assertIsDisplayed()

        composeTestRule.onNodeWithText("Confirmar compra").assertIsDisplayed()
    }

    @Test
    fun shoppingCart_MuestraMensajeVacio_CuandoNoHayProductos() {
        val cartViewModel = CartViewModel()

        composeTestRule.setContent {
            ShoppingCartScreen(
                cartViewModel = cartViewModel,
                onConfirm = {}
            )
        }

        composeTestRule.onNodeWithText("Tu carrito está vacío").assertIsDisplayed()
    }
}
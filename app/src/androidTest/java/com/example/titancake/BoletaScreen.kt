package com.example.titancake

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.example.titancake.data.model.Categoria
import com.example.titancake.data.model.Producto
import com.example.titancake.ui.screens.BoletaScreen
import com.example.titancake.ui.viewmodel.CartViewModel
import org.junit.Rule
import org.junit.Test

class BoletaScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun boletaScreen_CalculaImpuestosYTotal() {

        val cartViewModel = CartViewModel()
        val precioBase = 1000
        val cantidad = 1
        val nombreProducto = "Pastel Boleta"

        val productoPrueba = Producto(1, nombreProducto, precioBase, "D", 10, "", Categoria(1, "C"))
        cartViewModel.agregarProducto(productoPrueba, cantidad)


        val totalCompra = 1000
        val impuesto = 190 // 19% de 1000
        val totalFinal = 1190

        composeTestRule.setContent {
            BoletaScreen(cartViewModel = cartViewModel, onTimeout = {})
        }

        composeTestRule.onNodeWithText("----- BOLETA -----").assertIsDisplayed()
        composeTestRule.onNodeWithText(nombreProducto).assertIsDisplayed()
        composeTestRule.onAllNodesWithText("$ $totalCompra").assertCountEquals(2)

        composeTestRule.onNodeWithText("Total Compra").assertIsDisplayed()
        composeTestRule.onNodeWithText("$ $impuesto").assertIsDisplayed()
        composeTestRule.onNodeWithText("$ $totalFinal").assertIsDisplayed()

        composeTestRule.onNodeWithText("----- GRACIAS POR SU COMPRA -----").assertIsDisplayed()
    }
}
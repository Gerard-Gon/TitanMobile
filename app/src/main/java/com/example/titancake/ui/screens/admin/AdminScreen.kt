package com.example.titancake.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.titancake.data.model.CategoriaRequest
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import com.example.titancake.ui.components.TextFieldModificado
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.viewmodel.MainViewModel


// Esta pantalla muestra los detalles de un producto específico.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(authViewModel: AuthViewModel,  navControllerApp: NavHostController, viewModel: MainViewModel) {
    var producto by remember { mutableStateOf<Producto?>(null) }

    // Estados para cada campo del producto
    var nombreProducto by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcionProducto by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // 1. NUEVO ESTADO para el ID de la Categoría
    var categoriaIdInput by remember { mutableStateOf("") }
    // La constante CATEGORIA_ID_POR_DEFECTO ya no es necesaria aquí.


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BeigeP)
            )
        },
        containerColor = BeigeP
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .background(BeigeP)
        ) {
            // Campos de texto existentes
            TextFieldModificado(newValue = nombreProducto, onChange = { nombreProducto = it }, isPassword = false, label = "Nombre del producto")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = precio, onChange = { precio = it }, isPassword = false, label = "Precio")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = descripcionProducto, onChange = { descripcionProducto = it }, isPassword = false, label = "Descripción")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = stock, onChange = { stock = it }, isPassword = false, label = "Stock")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = imageUrl, onChange = { imageUrl = it }, isPassword = false, label = "URL de imagen")

            // 2. NUEVO CAMPO DE ENTRADA PARA CATEGORÍA
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(
                newValue = categoriaIdInput,
                onChange = { categoriaIdInput = it },
                isPassword = false,
                label = "ID de Categoría (Obligatorio)" // Se indica al usuario qué ingresar
            )

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                    val categoriaId = categoriaIdInput.toIntOrNull()
                    if (categoriaId != null && categoriaId > 0) {
                        val categoriaRequest = CategoriaRequest(id = categoriaId)
                        val nuevoProducto = ProductoRequest(
                            nombreProducto = nombreProducto,
                            precio = precio.toIntOrNull() ?: 0,
                            descripcionProducto = descripcionProducto,
                            stock = stock.toIntOrNull() ?: 0,
                            imageUrl = imageUrl,
                            categoria = categoriaRequest // Usa el ID ingresado
                        )
                        viewModel.addProducto(nuevoProducto) { success ->
                            if (success) {
                                // Éxito: Volver atrás
                                navControllerApp.popBackStack()
                            }
                        }
                    } else {
                        // Opcional: Mostrar un mensaje al usuario si el ID de Categoría es inválido.
                        println("Error: El ID de Categoría debe ser un número válido.")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrownP),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Producto", color = BeigeP)
            }
        }
    }
}
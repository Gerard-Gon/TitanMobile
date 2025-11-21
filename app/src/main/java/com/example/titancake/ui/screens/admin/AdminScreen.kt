package com.example.titancake.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
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
            // Campos de texto usando tu TextFieldModificado
            TextFieldModificado(newValue = nombreProducto, onChange = { nombreProducto = it }, isPassword = false, label = "Nombre del producto")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = precio, onChange = { precio = it }, isPassword = false, label = "Precio")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = descripcionProducto, onChange = { descripcionProducto = it }, isPassword = false, label = "Descripción")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = stock, onChange = { stock = it }, isPassword = false, label = "Stock")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = imageUrl, onChange = { imageUrl = it }, isPassword = false, label = "URL de imagen")

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                    val nuevoProducto = ProductoRequest(
                        nombreProducto = nombreProducto,
                        precio = precio.toIntOrNull() ?: 0,
                        descripcionProducto = descripcionProducto,
                        stock = stock.toIntOrNull() ?: 0,
                        imageUrl = imageUrl
                    )

                    viewModel.addProducto(nuevoProducto) { success ->
                        if (success) {
                            navControllerApp.popBackStack()
                        }
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
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
import coil.compose.AsyncImage
import com.example.titancake.data.model.Producto
import com.example.titancake.ui.components.TextFieldModificado
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.theme.Red
import com.example.titancake.ui.viewmodel.MainViewModel


// Esta pantalla muestra los detalles de un producto específico.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenAdmin(itemId: Int, viewModel: MainViewModel, onBack: () -> Unit) {
    var producto by remember { mutableStateOf<Producto?>(null) }

    // Estados editables
    var nombreProducto by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcionProducto by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }


    LaunchedEffect(itemId) {
        val p = viewModel.fetchProductoById(itemId)
        producto = p
        p?.let {
            nombreProducto = it.nombreProducto
            precio = it.precio.toString()
            descripcionProducto = it.descripcionProducto
            stock = it.stock.toString()
            imageUrl = it.imageUrl
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Admin", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
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
                .background(BeigeP),
        ) {
            if (producto != null) {
                AsyncImage(
                    model = producto!!.imageUrl,
                    contentDescription = "Imagen de ${producto!!.nombreProducto}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Campos editables
                TextFieldModificado(newValue = nombreProducto, onChange = { nombreProducto = it }, isPassword = false, label = "Nombre")
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldModificado(newValue = precio, onChange = { precio = it }, isPassword = false, label = "Precio")
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldModificado(newValue = descripcionProducto, onChange = { descripcionProducto = it }, isPassword = false, label = "Descripción")
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldModificado(newValue = stock, onChange = { stock = it }, isPassword = false, label = "Stock")
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldModificado(newValue = imageUrl, onChange = { imageUrl = it }, isPassword = false, label = "Imagen URL")
                Spacer(modifier = Modifier.height(30.dp))


            } else {
                Text("Cargando producto...", style = MaterialTheme.typography.bodyLarge)
            }

            Button(
                onClick = {
                    producto?.let {
                        val actualizado = it.copy(
                            nombreProducto = nombreProducto,
                            precio = precio.toIntOrNull() ?: 0,
                            descripcionProducto = descripcionProducto,
                            stock = stock.toIntOrNull() ?: 0,
                            imageUrl = imageUrl
                        )
                        viewModel.updateProducto(it.id, actualizado) { success ->
                            if (success) {
                                println("Producto actualizado correctamente")
                                onBack()
                            } else {
                                println("Error al actualizar producto")
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrownP),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Modificar Producto", color = BeigeP)
            }


            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    producto?.let {
                        viewModel.deleteProducto(it.id) { success ->
                            if (success) {
                                // Vuelve atrás si se borró correctamente
                                onBack()
                            } else {
                                println("Error al eliminar producto")
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar Producto", color = BeigeP)
            }

        }
    }
}
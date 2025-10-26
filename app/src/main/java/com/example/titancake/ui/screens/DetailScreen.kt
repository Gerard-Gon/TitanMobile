package com.example.titancake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.titancake.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Esta pantalla muestra los detalles de un producto específico.
fun DetailScreen(itemId: Int, viewModel: MainViewModel, onBack: () -> Unit) {
    val item = viewModel.getProducto(itemId) // Buscamos el producto usando su ID.

    // Usamos Scaffold para estructurar la pantalla con una barra superior.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") }, // Título que aparece en la parte superior.
                navigationIcon = {
                    // Botón de retroceso para volver a la pantalla anterior.
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        // Contenido principal de la pantalla.
        Column(
            modifier = Modifier
                .padding(padding) // Respetamos el espacio reservado por el Scaffold.
                .fillMaxSize()  // definimos el ancho.
                .padding(16.dp) // agregamos un margen para que se vea ordenado.
        ) {
            if (item != null) {
                // Mostramos el nombre del producto.
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                // Mostramos la descripción del producto.
                Text(item.descripcion, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                // Mostramos el precio del producto.
                Text(item.precio.toString(), style = MaterialTheme.typography.bodyMedium)

            } else {
                // Si no encontramos el producto, mostramos un mensaje de error.
                Text("Item no encontrado")
            }
        }
    }
}
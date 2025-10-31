package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.titancake.ui.theme.BeigeP
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
                title = { Text("Detalle", style = MaterialTheme.typography.titleLarge) }, // Título que aparece en la parte superior.
                navigationIcon = {
                    // Botón de retroceso para volver a la pantalla anterior.
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BeigeP
                )
            )
        },
        containerColor = BeigeP
    ) { padding ->
        // Contenido principal de la pantalla.
        Column(
            modifier = Modifier
                .padding(padding) // Respetamos el espacio reservado por el Scaffold.
                .fillMaxSize()  // definimos el ancho.
                .padding(16.dp) // agregamos un margen para que se vea ordenado.
                .background(BeigeP)
        ) {
            if (item != null) {
                AsyncImage(
                    model = item.productImg, // URL de la imagen.
                    contentDescription = "Imagen de ${item.nombre}", // Descripción para accesibilidad.
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)), // Bordes redondeados
                    contentScale = ContentScale.Crop // Recortamos la imagen para que se vea bien.
                )

                Spacer(modifier = Modifier.height(50.dp))

                // Mostramos el nombre del producto.
                Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontSize = 24.sp)
                Spacer(Modifier.height(8.dp))
                // Mostramos la descripción del producto.
                Text(item.descripcion, style = MaterialTheme.typography.bodyMedium, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                // Mostramos el precio del producto.
                Text("Precio: \$${item.precio}", style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)

            } else {
                // Si no encontramos el producto, mostramos un mensaje de error.
                Text("Item no encontrado")
            }
        }
    }
}
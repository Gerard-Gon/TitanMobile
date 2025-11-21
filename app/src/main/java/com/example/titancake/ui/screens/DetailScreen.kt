package com.example.titancake.ui.screens

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
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Esta pantalla muestra los detalles de un producto específico.
fun DetailScreen(itemId: Int, viewModel: MainViewModel, onBack: () -> Unit) {
    var producto by remember { mutableStateOf<Producto?>(null) }

    LaunchedEffect(itemId) {
        producto = viewModel.fetchProductoById(itemId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Producto", style = MaterialTheme.typography.titleLarge) },
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
                .background(BeigeP)
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

                Spacer(modifier = Modifier.height(50.dp))

                Text(producto!!.nombreProducto, style = MaterialTheme.typography.titleMedium, fontSize = 24.sp)
                Spacer(Modifier.height(8.dp))
                Text(producto!!.descripcionProducto, style = MaterialTheme.typography.bodyMedium, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text("Precio: \$${producto!!.precio}", style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(32.dp))
            } else {
                Text("Cargando producto...", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
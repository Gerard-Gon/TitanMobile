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
fun DetailScreen(itemId: Int, viewModel: MainViewModel, onBack: () -> Unit) {
    val item = viewModel.getProducto(itemId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (item != null) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(item.descripcion, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text(item.precio.toString(), style = MaterialTheme.typography.bodyMedium)

            } else {
                Text("Item no encontrado")
            }
        }
    }
}
package com.example.titancake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.titancake.ui.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    cartViewModel: CartViewModel,
    onBack: () -> Unit
) {
    val productos = cartViewModel.carrito.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (productos.value.isEmpty()) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                productos.value.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = item.producto.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Cantidad: ${item.cantidad}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Subtotal: \$${item.producto.precio * item.cantidad}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Row {
                            Button(
                                onClick = { cartViewModel.quitarUnidad(item.producto.id) },
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Text("-")
                            }
                            Button(
                                onClick = { cartViewModel.agregarProducto(item.producto, 1) },
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Text("+")
                            }
                            Button(
                                onClick = { cartViewModel.eliminarProducto(item.producto.id) },
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Text("Eliminar")
                            }


                        }
                    }
                    Divider()
                }

                Text(
                    text = "Total: \$${cartViewModel.total()}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                TopAppBar(
                    title = { Text("Comprar") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.Done, contentDescription = "Comprar")
                        }
                    }
                )

            }
        }
    }
}

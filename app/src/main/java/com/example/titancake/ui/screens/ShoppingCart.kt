package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.Blue
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.viewmodel.CartViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Esta pantalla muestra el carrito de compras del usuario.
fun ShoppingCartScreen(
    cartViewModel: CartViewModel, // ViewModel que maneja la lógica del carrito.
    onBack: () -> Unit,
    onConfirm: () -> Unit
// Acción que se ejecuta al tocar el botón de retroceso.
) {
    // Obtenemos la lista de productos en el carrito en tiempo real.
    val productos = cartViewModel.carrito.collectAsState()
    // Variable que indica si estamos en proceso de compra
    var isLoading by remember { mutableStateOf(false) }

    // Si el usuario confirma la compra, esperamos 3 segundos y vaciamos el carrito.
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(3000)
            onConfirm()
            isLoading = false // vuelve al estado normal
        }
    }


    // Estructura principal de la pantalla con barra superior.
    Scaffold(containerColor = BeigeP,
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

            Column(modifier = Modifier.padding(padding).background(BeigeP)) {
                // Si el carrito está vacío, mostramos un mensaje.
                if (productos.value.isEmpty()) {
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
            } else {
                    // Mostramos cada producto en el carrito.
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
                        // Botones para modificar la cantidad o eliminar el producto.
                        Row {
                            Button(
                                onClick = { cartViewModel.quitarUnidad(item.producto.id) },
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrownP)
                            ) {
                                Text("-")
                            }
                            Button(
                                onClick = { cartViewModel.agregarProducto(item.producto, 1) },
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrownP)
                            ) {
                                Text("+")
                            }
                            Button(
                                onClick = { cartViewModel.eliminarProducto(item.producto.id) },
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrownP)
                            ) {
                                Text("Eliminar")
                            }


                        }
                    }
                    Divider()
                }
                    // Mostramos el total a pagar.
                Text(
                    text = "Total a Pagar: \$${cartViewModel.total()}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                        .background(BeigeP)
                )
                    // Botón para confirmar la compra.

                    Button(
                    onClick = {
                        isLoading = true
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrownP),
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {

                    if (isLoading) {
                        // Mostramos un indicador de carga mientras se procesa la compra.
                        CircularProgressIndicator(
                            color = Blue,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        // Ícono y texto del botón cuando está listo para confirmar.
                        Icon(Icons.Default.Check, contentDescription = "Confirmar compra")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirmar compra", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}



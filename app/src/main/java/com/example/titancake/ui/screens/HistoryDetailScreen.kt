package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.titancake.data.model.ItemCarritoBackend
import com.example.titancake.data.remote.RetrofitInstance
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.theme.Green
import com.example.titancake.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(navController: NavController, carritoId: Int) {
    var itemsCompra by remember { mutableStateOf<List<ItemCarritoBackend>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // --- CÁLCULOS DE PRECIO E IMPUESTO ---
    // 1. Sumamos el valor de los productos (Neto/Subtotal)
    val subtotal = itemsCompra.sumOf { it.precioUnitario * it.cantidad }

    // 2. Calculamos el 19% de IVA (Convertimos a Int para no tener decimales)
    val iva = (subtotal * 0.19).toInt()

    // 3. Sumamos para el total real pagado
    val totalFinal = subtotal + iva

    LaunchedEffect(carritoId) {
        try {
            // Descargamos todo y filtramos (Estrategia Opción B)
            val todosLosItems = RetrofitInstance.api.getAllVentas()
            itemsCompra = todosLosItems.filter { it.carrito.id == carritoId }
        } catch (e: Exception) {
            println("Error al cargar detalle: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Compra #$carritoId", color = BrownP, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = BrownP)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BeigeP)
            )
        },
        containerColor = BeigeP,
        bottomBar = {
            // Barra inferior con el desglose de precios
            if (!isLoading && itemsCompra.isNotEmpty()) {
                Surface(
                    color = White,
                    shadowElevation = 16.dp,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Fila Subtotal
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal:", color = Color.Gray)
                            Text("$ $subtotal", color = Color.Gray)
                        }

                        // Fila IVA
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("IVA (19%):", color = Color.Gray)
                            Text("$ $iva", color = Color.Gray)
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Fila Total Final
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Pagado:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("$ $totalFinal", style = MaterialTheme.typography.headlineSmall, color = Green, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrownP)
                }
            } else if (itemsCompra.isEmpty()) {
                Text("No se encontraron detalles para esta compra.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(itemsCompra) { item ->
                        ItemDetalleRow(item)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemDetalleRow(item: ItemCarritoBackend) {
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.producto.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = item.producto.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.producto.nombreProducto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = BrownP
                )
                Text(
                    text = "${item.cantidad} x $ ${item.precioUnitario}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Text(
                text = "$ ${item.cantidad * item.precioUnitario}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
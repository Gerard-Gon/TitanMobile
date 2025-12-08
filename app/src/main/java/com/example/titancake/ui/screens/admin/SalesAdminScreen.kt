package com.example.titancake.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.titancake.data.model.VentaAgrupada
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.theme.Green
import com.example.titancake.ui.theme.White
import com.example.titancake.ui.viewmodel.AdminSalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreenAdmin(viewModel: AdminSalesViewModel = viewModel()) {

    val ventas by viewModel.ventas.collectAsState()
    val totalHistorico by viewModel.totalHistorico.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Cargar datos al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarVentas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reporte de Ventas", fontWeight = FontWeight.Bold, color = BrownP) },
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
        ) {
            // --- TARJETA DE TOTAL ACUMULADO ---
            Card(
                colors = CardDefaults.cardColors(containerColor = BrownP),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("VENTAS TOTALES", color = BeigeP, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Green, modifier = Modifier.size(36.dp))
                        Text(
                            text = "$ $totalHistorico",
                            color = White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text("Historial de Pedidos", style = MaterialTheme.typography.titleMedium, color = BrownP)
            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrownP)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ventas) { venta ->
                        VentaItemRow(venta)
                    }
                }
            }
        }
    }
}

@Composable
fun VentaItemRow(venta: VentaAgrupada) {
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado: ID Pedido y Cliente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pedido #${venta.carritoId}",
                    fontWeight = FontWeight.Bold,
                    color = BrownP,
                    fontSize = 18.sp
                )
                Text(
                    text = venta.nombreUsuario,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = BeigeP)

            // Lista de productos (breve)
            venta.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.cantidad}x ${item.producto.nombreProducto}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "$ ${item.cantidad * item.precioUnitario}", // Precio neto del item
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = BeigeP)

            // --- DESGLOSE DE TOTALES (NUEVO) ---

            // Subtotal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", fontSize = 14.sp, color = Color.Gray)
                Text("$ ${venta.subtotal}", fontSize = 14.sp, color = Color.Gray)
            }

            // IVA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("IVA (19%)", fontSize = 14.sp, color = Color.Gray)
                Text("$ ${venta.totalConIva - venta.subtotal}", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Total Final Destacado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("TOTAL PAGADO", fontWeight = FontWeight.Bold, color = BrownP)
                Text(
                    text = "$ ${venta.totalConIva}",
                    fontWeight = FontWeight.Bold,
                    color = Green,
                    fontSize = 18.sp
                )
            }
        }
    }
}
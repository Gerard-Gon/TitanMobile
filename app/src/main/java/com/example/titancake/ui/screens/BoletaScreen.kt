package com.example.titancake.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.titancake.ui.theme.BrownP
import kotlinx.coroutines.delay
import com.example.titancake.R
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.White
import com.example.titancake.ui.viewmodel.CartViewModel


@Composable
// Esta pantalla se muestra brevemente al iniciar la app.
fun BoletaScreen(cartViewModel: CartViewModel,onTimeout: () -> Unit) {

    val productos = cartViewModel.carrito.collectAsState()
    val totalCompra = cartViewModel.total()
    val impuesto = totalCompra * 0.19
    val totalFinal = totalCompra + impuesto

    // Usamos LaunchedEffect para esperar 2 segundos y luego ejecutar la acción de salida
    LaunchedEffect(true) {
        delay(5000)
        onTimeout()
        cartViewModel.vaciarCarrito()
    }

    // Caja principal que ocupa toda la pantalla.
    Scaffold(containerColor = BeigeP) {

        Column(modifier = Modifier.padding(16.dp) )
        {

        Spacer(modifier = Modifier.height(20.dp))

        Text("----- BOLETA -----",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(40.dp))

        productos.value.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.producto.nombre, style = MaterialTheme.typography.bodyLarge)
                Text(text = "$ ${item.producto.precio * item.cantidad}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total Compra", fontWeight = FontWeight.Bold)
            Text("$ ${"%.1f".format(totalCompra)}")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Impuesto (19%)", fontWeight = FontWeight.Bold)
            Text("$ ${"%.1f".format(impuesto)}")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total Final", fontWeight = FontWeight.Bold)
            Text("$ ${"%.1f".format(totalFinal)}")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text("----- GRACIAS POR SU COMPRA -----",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge)
    }

  }
}
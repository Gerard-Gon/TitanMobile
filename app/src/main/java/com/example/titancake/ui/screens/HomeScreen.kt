package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.titancake.data.model.Producto
import com.example.titancake.navigation.Routes
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel,cartViewModel: CartViewModel ,onItemClick: (Int) -> Unit, onClick: () -> Unit, navController: NavHostController ) {
    val productos = viewModel.productos.collectAsState()

    Column(modifier = Modifier.fillMaxSize()
        .background(BeigeP)) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth() .background(BeigeP),
                    contentAlignment = Alignment.Center,

                ) {
                    Text(
                        text = "TITANCAKE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = BrownP,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    )
                }
            },
            )

        TopAppBar(
            title = { Text("Bienvenido a TitanCake", fontWeight = FontWeight.Bold)  },
            actions = {
                IconButton (onClick = { navController.navigate(Routes.SHOPPINGCART) }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }
                IconButton(onClick = { navController.navigate(Routes.REGISTER) }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Login")
                }
            }
        )

        TopAppBar(title = { Text("Lista de Productos") })
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(productos.value) { item ->
                ItemRow(item = item, onClick = { onItemClick(item.id) },
                    onAddToCart = { cantidad -> cartViewModel.agregarProducto(item, cantidad) }
                )
                Divider()
            }
        }
    }
}

@Composable
fun ItemRow(item: Producto, onClick: () -> Unit, onAddToCart: (Int) -> Unit) {
    var cantidad by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(item.nombre, style = MaterialTheme.typography.titleMedium)
        Text(item.descripcion, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
        Text("Precio: \$${item.precio}", style = MaterialTheme.typography.bodySmall)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { if (cantidad > 1) cantidad-- }) { Text("-") }
            Text("$cantidad", modifier = Modifier.padding(horizontal = 8.dp))
            Button(onClick = { cantidad++ }) { Text("+") }
        }

        Button(onClick = { onAddToCart(cantidad) }, modifier = Modifier.padding(top = 4.dp)) {
            Text("Agregar al carrito")
        }
    }
}
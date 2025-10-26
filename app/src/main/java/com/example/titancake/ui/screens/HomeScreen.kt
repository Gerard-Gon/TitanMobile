package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.titancake.data.model.Producto
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Esta es la pantalla principal de TitanCake.
fun HomeScreen(viewModel: MainViewModel,cartViewModel: CartViewModel ,onItemClick: (Int) -> Unit, onClick: () -> Unit, navController: NavHostController ) {

    // Obtenemos la lista de productos en tiempo real.
    val productos = viewModel.productos.collectAsState()

    // Usamos Scaffold para estructurar la pantalla con una barra superior.
    Scaffold (topBar =
    {
        TopAppBar(
            title = {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "TITANCAKE MOBILE", // Nombre de la app.
                        fontWeight = FontWeight.Bold, // Texto en negrita.
                        fontSize = 37.sp, // Tamaño grande para destacar.
                        color = BrownP, // Color personalizado.
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    )
                }
            }
        )
    }){ padding->
        // Contenido principal de la pantalla.
        Column(modifier = Modifier.fillMaxSize()
        .background(BeigeP)  // Fondo cálido y suave
        .padding(padding) // Respeta el espacio reservado por el Scaffold.
        ) {

            // Lista vertical que muestra cada producto.
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(productos.value) { item ->
                // Mostramos cada producto con su imagen, nombre, descripción y botón para agregar al carrito.
                ItemRow(item = item, onClick = { onItemClick(item.id) },  // Al tocar el producto, vamos a su detalle.
                    onAddToCart = { cantidad -> cartViewModel.agregarProducto(item, cantidad) }
                )
                Divider() // Línea separadora entre productos.

            }
        }
    }
}

}

@Composable
// Este componente muestra un solo producto en la lista.
fun ItemRow(item: Producto, onClick: () -> Unit, onAddToCart: (Int) -> Unit) {
    var cantidad by remember { mutableStateOf(1) } // Variable que guarda cuántas unidades quiere el usuario.


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Al tocar el producto, ejecutamos la acción de detalle.
            .padding(8.dp) // Margen interno para que se vea ordenado.
    ) {
        // Si el producto tiene imagen, la mostramos.
        if (item.productImg.isNotEmpty()) {
            AsyncImage(
                model = item.productImg, // URL de la imagen.
                contentDescription = "Imagen de ${item.nombre}", // Descripción para accesibilidad.
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)), // Bordes redondeados
                contentScale = ContentScale.Crop // Recortamos la imagen para que se vea bien.
            )
        }

        // Mostramos el nombre del producto.
        Text(item.nombre, style = MaterialTheme.typography.titleMedium)
        // Mostramos la descripción (máximo 3 líneas).
        Text(item.descripcion, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
        // Mostramos el precio.
        Text("Precio: \$${item.precio}", style = MaterialTheme.typography.bodySmall)

        // Controles para elegir la cantidad.
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { if (cantidad > 1) cantidad-- }) { Text("-") } // Disminuir cantidad.
            Text("$cantidad", modifier = Modifier.padding(horizontal = 8.dp)) // Mostrar cantidad actual.
            Button(onClick = { cantidad++ }) { Text("+") } // Aumentar cantidad.

        }

        // Botón para agregar el producto al carrito con la cantidad elegida.
        Button(onClick = { onAddToCart(cantidad) }, modifier = Modifier.padding(top = 4.dp)) {
            Text("Agregar al carrito")
        }
    }
}

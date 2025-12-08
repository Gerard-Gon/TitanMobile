package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.titancake.data.model.CarritoResponse
import com.example.titancake.data.remote.RetrofitInstance
import com.example.titancake.navigation.Routes
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, authViewModel: AuthViewModel) {
    val usuario = authViewModel.currentUserBackend
    // Estado para la lista de compras filtrada
    var historial by remember { mutableStateOf<List<CarritoResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (usuario != null) {
            try {
                // 1. Obtenemos TODOS los carritos del backend (Opción B)
                val todosLosCarritos = RetrofitInstance.api.getCarritos()

                // 2. Filtramos en el celular: Solo los que coinciden con mi ID
                historial = todosLosCarritos.filter {
                    it.usuario.id == usuario.id
                }.sortedByDescending { it.id } // Ordenamos del más reciente al más antiguo

            } catch (e: Exception) {
                println("Error cargando historial: ${e.message}")
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Compras", color = BrownP) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = BrownP)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BeigeP)
            )
        },
        containerColor = BeigeP
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrownP)
                }
            } else if (historial.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aún no has realizado compras.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(historial) { compra ->
                        Card(
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                    onClick = {
                                navController.navigate(Routes.historyDetailRoute(compra.id))
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = BrownP)
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(text = "Compra #${compra.id}", style = MaterialTheme.typography.titleMedium, color = BrownP)
                                    Text(text = "Cliente: ${usuario?.nombre}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
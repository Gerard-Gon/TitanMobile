package com.example.titancake.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

// Esta clase representa cada ítem del menú inferior (bottom navigation bar).
sealed class BottomNavItemAdmin(val route: String, val label: String, val icon: ImageVector) {

    // Sección principal donde se muestran los productos o recetas.
    object Home : BottomNavItem(Routes.HOME, "Inicio", Icons.Default.Home)

    // Sección donde el usuario puede ver su perfil.
    object Profile : BottomNavItem(Routes.PROFILE, "Perfil", Icons.Default.Person)

    //  Sección donde se muestran los productos que el usuario quiere comprar.
    object ShoppingCart : BottomNavItem(Routes.SHOPPINGCART, "Carrito", Icons.Default.ShoppingCart)

    object AdministrarProducto : BottomNavItem(Routes.ADMIN, "Admin", Icons.Default.Add)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Esta función dibuja la barra de navegación inferior (BottomBar).
fun BottomBarAdmin(navController: NavHostController, items: List<BottomNavItem>) {
    // Obtenemos la ruta actual para saber qué ítem está seleccionado.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    // Dibujamos la barra de navegación con todos los ítems que recibimos.
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                // Mostramos el ícono del ítem
                icon = { Icon(item.icon, contentDescription = item.label) },
                // Mostramos el texto debajo del ícono.
                label = { Text(item.label) },
                // Marcamos este ítem como seleccionado si coincide con la ruta actual.
                selected = currentRoute == item.route,
                // Cuando el usuario toca un ítem, navegamos a su ruta si no estamos ya ahí.
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Volvemos al inicio del gráfico de navegación si es necesario.
                            popUpTo(navController.graph.startDestinationRoute ?: Routes.HOME) {
                                saveState = true // Guardamos el estado anterior.

                            }
                            launchSingleTop = true // Evitamos duplicar pantallas.

                            restoreState = true // Restauramos el estado si ya habíamos estado ahí.
                        }
                    }
                }
            )
        }
    }
}
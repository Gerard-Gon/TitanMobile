package com.example.titancake.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.titancake.ui.screens.BoletaScreen
import com.example.titancake.ui.screens.DetailScreen
import com.example.titancake.ui.screens.HomeScreen
import com.example.titancake.ui.screens.LoginScreen
import com.example.titancake.ui.screens.PostScreen
import com.example.titancake.ui.screens.ProfileScreen
import com.example.titancake.ui.screens.RegisterScreen
import com.example.titancake.ui.screens.ShoppingCartScreen
import com.example.titancake.ui.screens.SplashScreen
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.MainViewModel

@Composable
// Esta función define toda la navegación de la app TitanCake.
fun AppNavGraph(authViewModel: AuthViewModel, isLoggedIn: Boolean) {
    // Creamos el controlador de navegación, que nos permite movernos entre pantallas.
    val navController = rememberNavController()
    // Creamos el ViewModel del carrito, que guarda los productos.
    val cartViewModel: CartViewModel = viewModel()
    // Definimos los ítems que aparecerán en la barra inferior de navegación.
    val bottomItems = listOf(BottomNavItem.Home, BottomNavItem.ShoppingCart,BottomNavItem.Profile)
    // Estas son las rutas donde queremos que se muestre la barra inferior.
    val showBottomBarRoutes = listOf(Routes.HOME, Routes.PROFILE, Routes.SHOPPINGCART)
    // Obtenemos la ruta actual para saber si debemos mostrar la barra inferior.
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in showBottomBarRoutes

    // Usamos Scaffold para estructurar la pantalla y agregar la barra inferior si corresponde.
    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomBar(navController = navController, items = bottomItems)
        }
    ) { innerPadding ->

    // Aqui definimos todas las rutas posibles de la app.
    NavHost(
        navController = navController,
        startDestination = "splash", // Pantalla inicial
        modifier = Modifier.padding(innerPadding)
    ) {
        // Pantalla de presentacion (Splash)
        composable("splash") {
            SplashScreen {
                // después de mostrar el splash, decidimos a donde ir:
                // Si el usuaario esta logueado, lo llevamos al "home" si no a la pantalla de "login"
                if (isLoggedIn) navController.navigate("home") { popUpTo("splash") { inclusive = true } }
                else navController.navigate("login") { popUpTo("splash") { inclusive = true } }
            }
        }

        // Pantallla de inicio de sesion.
        composable("login") {
            LoginScreen(
                onLogin = { email, pass -> authViewModel.login(email, pass) },
                onNavigateToRegister = { navController.navigate("register") },
                onSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        // Pantalla de registro de usuario.
        composable("register") {
            RegisterScreen(
                onRegister = { email, pass, name, confirmpass ->
                    authViewModel.register(email, pass, name, confirmpass)
                },
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        // Pantalla principal donde se muestran los productos
        composable(Routes.HOME) {
            val vm: MainViewModel = viewModel()
            HomeScreen(viewModel = vm, cartViewModel = cartViewModel,onItemClick = { id -> // Si el usuario toca un producto, lo llevamos a la pantalla de detalle.
                navController.navigate(Routes.detailRoute(id))
            }, onClick = {

            }, navController = navController)
        }

        // Pantalla de perfil del usuario.
        composable(Routes.PROFILE) {
            ProfileScreen(authViewModel = authViewModel, navControllerApp = navController)
        }

        // Pantalla de carrito de compras.
        composable(Routes.SHOPPINGCART) {
            ShoppingCartScreen(
                cartViewModel = cartViewModel,
                onConfirm = { navController.navigate("boleta") }
            )
        }

        composable("cart") {
            ShoppingCartScreen(
                cartViewModel = cartViewModel,
                onConfirm = { navController.navigate("boleta") }
            )
        }
        composable("boleta") {
            BoletaScreen(
                cartViewModel = cartViewModel,
                onTimeout = {
                    navController.navigate("home") {
                        popUpTo("boleta") { inclusive = true } // elimina boleta del backstack
                    }
                }
            )

        }

        // Pantalla de detalle de un producto específico
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val vm: MainViewModel = viewModel()
            val id = backStackEntry.arguments?.getInt("itemId") ?: -1
            DetailScreen(itemId = id, viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable("get") {
            val viewModel: MainViewModel = viewModel()
            PostScreen(viewModel = viewModel)
        }

    }
}

}
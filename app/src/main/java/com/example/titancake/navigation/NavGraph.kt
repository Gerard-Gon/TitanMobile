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
import com.example.titancake.data.repository.AuthRepository
import com.example.titancake.ui.screens.BoletaScreen
import com.example.titancake.ui.screens.DetailScreen
import com.example.titancake.ui.screens.HistoryDetailScreen
import com.example.titancake.ui.screens.HistoryScreen
import com.example.titancake.ui.screens.HomeScreen
import com.example.titancake.ui.screens.LoginScreen
import com.example.titancake.ui.screens.ProfileScreen
import com.example.titancake.ui.screens.RegisterScreen
import com.example.titancake.ui.screens.ShoppingCartScreen
import com.example.titancake.ui.screens.SplashScreen
import com.example.titancake.ui.screens.admin.AdminScreen
import com.example.titancake.ui.screens.admin.DetailScreenAdmin
import com.example.titancake.ui.screens.admin.HomeScreenAdmin
import com.example.titancake.ui.screens.admin.ProfileScreenAdmin
import com.example.titancake.ui.screens.admin.SalesScreenAdmin
import com.example.titancake.ui.screens.admin.ShoppingCartScreenAdmin
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.CartViewModelFactory
import com.example.titancake.ui.viewmodel.MainViewModel

@Composable
// Esta función define toda la navegación de la app TitanCake.
fun AppNavGraph(authViewModel: AuthViewModel, isLoggedIn: Boolean, authRepository: AuthRepository) {
    // Creamos el controlador de navegación, que nos permite movernos entre pantallas.
    val navController = rememberNavController()
    // Creamos el ViewModel del carrito, que guarda los productos.
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(authRepository)
    )
    // Definimos los ítems que aparecerán en la barra inferior de navegación.
    val bottomItems = listOf(BottomNavItem.Home, BottomNavItem.ShoppingCart,BottomNavItem.Profile)
    val bottomItemsAdmin = listOf(
        BottomNavItemAdmin.Home,
        BottomNavItemAdmin.Ventas,
        BottomNavItemAdmin.Profile,
        BottomNavItemAdmin.ShoppingCart,
        BottomNavItemAdmin.AdministrarProducto
    )
    // Estas son las rutas donde queremos que se muestre la barra inferior.
    val showBottomBarRoutes = listOf(Routes.HOME, Routes.PROFILE, Routes.SHOPPINGCART)
    val showBottomBarAdminRoutes = listOf(Routes.HOMEADMIN, Routes.PROFILEADMIN, Routes.SHOPPINGCARTADMIN, Routes.ADMIN, Routes.SALES_ADMIN)
    // Obtenemos la ruta actual para saber si debemos mostrar la barra inferior.
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in showBottomBarRoutes
    val showBottomBarAdmin = currentRoute in showBottomBarAdminRoutes

    // Usamos Scaffold para estructurar la pantalla y agregar la barra inferior si corresponde.
    Scaffold(
        bottomBar = {
            when {
                showBottomBarAdmin -> BottomBarAdmin(navController = navController, items = bottomItemsAdmin)
                showBottomBar -> BottomBar(navController = navController, items = bottomItems)
            }

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
                if (isLoggedIn) {
                    if (authViewModel.isAdmin) {
                        navController.navigate("homeAdmin") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                } else {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }

        // Pantallla de inicio de sesion.
        composable("login") {
            LoginScreen(
                onLogin = { email, pass -> authViewModel.login(email, pass) },
                onNavigateToRegister = { navController.navigate("register") },
                onSuccessCliente = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSuccessAdmin = {
                    navController.navigate("homeAdmin") {
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

        composable(Routes.HOMEADMIN) {
            val vm: MainViewModel = viewModel()
            HomeScreenAdmin(viewModel = vm, cartViewModel = cartViewModel,onItemClick = { id -> // Si el usuario toca un producto, lo llevamos a la pantalla de detalle.
                navController.navigate(Routes.detailAdminRoute(id))
            }, onClick = {

            }, navController = navController)
        }


        composable(Routes.PROFILEADMIN) {
            ProfileScreenAdmin(authViewModel = authViewModel, navControllerApp = navController)
        }

        composable(Routes.SHOPPINGCARTADMIN) {
            ShoppingCartScreenAdmin(
                cartViewModel = cartViewModel,
                onConfirm = { navController.navigate("boleta") }
            )
        }

        composable(Routes.ADMIN) {
            val mainViewModel: MainViewModel = viewModel()
            AdminScreen(
                authViewModel = authViewModel,
                navControllerApp = navController,
                viewModel = mainViewModel
            )
        }


        composable(
            route = Routes.DETAILADMIN,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val vm: MainViewModel = viewModel()
            val id = backStackEntry.arguments?.getInt("itemId") ?: -1
            DetailScreenAdmin(itemId = id, viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Routes.SALES_ADMIN) {
            SalesScreenAdmin()
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

        composable(Routes.HISTORY) {
            HistoryScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = Routes.HISTORY_DETAIL,
            arguments = listOf(androidx.navigation.navArgument("carritoId") { type = androidx.navigation.NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("carritoId") ?: -1
            // Llamamos a la nueva pantalla pasándole el ID
            HistoryDetailScreen(navController = navController, carritoId = id)
        }


    }
}

}
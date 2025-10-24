package com.example.titancake.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.titancake.ui.screens.DetailScreen
import com.example.titancake.ui.screens.HomeScreen
import com.example.titancake.ui.screens.LoginScreen
import com.example.titancake.ui.screens.ProfileScreen
import com.example.titancake.ui.screens.RegisterScreen
import com.example.titancake.ui.screens.ShoppingCartScreen
import com.example.titancake.ui.screens.SplashScreen
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.MainViewModel

@Composable
fun AppNavGraph(authViewModel: AuthViewModel, isLoggedIn: Boolean) {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val bottomItems = listOf(BottomNavItem.Home, BottomNavItem.ShoppingCart,BottomNavItem.Profile)
    val showBottomBarRoutes = listOf(Routes.HOME, Routes.PROFILE, Routes.SHOPPINGCART)
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in showBottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomBar(navController = navController, items = bottomItems)
        }
    ) { innerPadding ->

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.padding(innerPadding)
    ) {

        composable("splash") {
            SplashScreen {
                // después de splash, decide:
                if (isLoggedIn) navController.navigate("home") { popUpTo("splash") { inclusive = true } }
                else navController.navigate("login") { popUpTo("splash") { inclusive = true } }
            }
        }

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

        composable(Routes.HOME) {
            val vm: MainViewModel = viewModel()
            HomeScreen(viewModel = vm, cartViewModel = cartViewModel,onItemClick = { id ->
                navController.navigate(Routes.detailRoute(id))
            }, onClick = {

            }, navController = navController)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(authViewModel = authViewModel, navControllerApp = navController)
        }


        composable(Routes.SHOPPINGCART) {
            ShoppingCartScreen(
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val vm: MainViewModel = viewModel()
            val id = backStackEntry.arguments?.getInt("itemId") ?: -1
            DetailScreen(itemId = id, viewModel = vm, onBack = { navController.popBackStack() })
        }

    }
}

}
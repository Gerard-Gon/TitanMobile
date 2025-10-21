package com.example.titancake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.titancake.navigation.BottomBar
import com.example.titancake.navigation.BottomNavItem
import com.example.titancake.navigation.Routes
import com.example.titancake.ui.screens.DetailScreen
import com.example.titancake.ui.screens.HomeScreen
import com.example.titancake.ui.screens.ProfileScreen
import com.example.titancake.ui.screens.RegisterScreen
import com.example.titancake.ui.screens.ShoppingCartScreen
import com.example.titancake.ui.theme.TitanCakeTheme
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TitanCakeTheme { App() }
        }
    }
}
@Composable
fun App() {
    val navController = rememberNavController()
    val bottomItems = listOf(BottomNavItem.Home, BottomNavItem.Profile)
    val cartViewModel: CartViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomBar(navController, bottomItems) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                val vm: MainViewModel = viewModel()
                HomeScreen(viewModel = vm, cartViewModel = cartViewModel,onItemClick = { id ->
                    navController.navigate(Routes.detailRoute(id))
                }, onClick = {

                }, navController = navController)
            }

            composable(Routes.PROFILE) { ProfileScreen() }

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
            composable(
                route = Routes.REGISTER,
            ) { backStackEntry ->
                RegisterScreen (onBack ={ navController.popBackStack() })


            }

        }
    }
}
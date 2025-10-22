package com.example.titancake.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.titancake.ui.screens.user.AddScreen
import com.example.titancake.ui.screens.HomeScreen
import com.example.titancake.ui.screens.ProfileScreen
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.theme.*
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.MainViewModel

@Composable
fun NavHome(authViewModel: AuthViewModel,mainViewModel: MainViewModel, cartViewModel: CartViewModel, navControllerApp: NavHostController) {
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableStateOf("home_user") }


    Scaffold(
        containerColor = Black,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets,   containerColor = BrownP,
                contentColor = Gray) {
                NavigationBarItem(
                    selected = selectedDestination == "home_user",
                    onClick = {
                        navController.navigate(route = "home_user")
                        selectedDestination = "home_user"
                    },
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "home_user",
                        )
                    },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = White,
                        selectedTextColor = White,
                        unselectedIconColor = Gray,
                        unselectedTextColor = Gray,
                        indicatorColor = Color.Transparent
                    )

                )
                NavigationBarItem(
                    selected = selectedDestination == "add",
                    onClick = {
                        navController.navigate(route = "add")
                        selectedDestination = "add"
                    },
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "add",
                            tint = White
                        )
                    },
                    label = { Text("Agregar") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = White,
                        selectedTextColor = White,
                        unselectedIconColor = Gray,
                        unselectedTextColor = Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    selected = selectedDestination == "perfil",
                    onClick = {
                        navController.navigate(route = "perfil")
                        selectedDestination = "perfil"
                    },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "perfil",

                            )
                    },
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = White,
                        selectedTextColor = White,
                        unselectedIconColor = Gray,
                        unselectedTextColor = Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            } }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home_user",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home_user") { HomeScreen(viewModel = mainViewModel, cartViewModel = cartViewModel, onItemClick = {}, onClick = {}, navController = navControllerApp) }
            composable("add") { AddScreen() }
            composable("perfil") { ProfileScreen(authViewModel, navControllerApp) }
        }
    }
}
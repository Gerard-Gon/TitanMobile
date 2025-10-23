package com.example.titancake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.titancake.data.repository.AuthRepository
import com.example.titancake.navigation.AppNavGraph
import com.example.titancake.navigation.BottomBar
import com.example.titancake.navigation.BottomNavItem
import com.example.titancake.navigation.Routes
import com.example.titancake.ui.screens.DetailScreen
import com.example.titancake.ui.screens.HomeScreen
import com.example.titancake.ui.screens.ProfileScreen
import com.example.titancake.ui.screens.RegisterScreen
import com.example.titancake.ui.screens.ShoppingCartScreen
import com.example.titancake.ui.theme.TitanCakeTheme
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.viewmodel.AuthViewModelFactory
import com.example.titancake.ui.viewmodel.CartViewModel
import com.example.titancake.ui.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciamos Firebase y el repositorio
        val repository = AuthRepository(applicationContext)
        val authViewModel: AuthViewModel by viewModels {
            AuthViewModelFactory(repository)
        }

        // Verificamos si el usuario ya está logueado
        val currentUser = FirebaseAuth.getInstance().currentUser
        val isLoggedIn = currentUser != null

        enableEdgeToEdge()
        setContent {
            TitanCakeTheme { AppNavGraph(
                authViewModel = authViewModel,
                isLoggedIn = isLoggedIn
            ) }
        }
    }
}

package com.example.titancake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.titancake.data.repository.AuthRepository
import com.example.titancake.navigation.AppNavGraph
import com.example.titancake.ui.theme.TitanCakeTheme
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.viewmodel.AuthViewModelFactory
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
                isLoggedIn = isLoggedIn,
                authRepository = repository
            ) }
        }
    }
}


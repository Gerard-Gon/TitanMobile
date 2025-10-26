package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.titancake.data.repository.AuthRepository

// Esta clase se encarga de crear instancias de AuthViewModel.
class AuthViewModelFactory(
    private val repository: AuthRepository // Repositorio que maneja la lógica de autenticación.
) : ViewModelProvider.Factory {

    // Esta función se llama cuando Android necesita crear un ViewModel.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verificamos si el ViewModel que se quiere crear es AuthViewModel.
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Si es así, lo creamos usando el repositorio que recibimos.
            return AuthViewModel(repository) as T
        }
        // Si el tipo de ViewModel no es compatible, lanzamos una excepción.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titancake.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Esta clase representa los distintos estados posibles durante el proceso de autenticación.
sealed class AuthState {
    object Idle : AuthState() // Estado inicial, sin actividad.
    object Loading : AuthState() // Estado mientras se está procesando login o registro.

    data class Success(val uid: String) : AuthState()  // Estado cuando el login o registro fue exitoso.

    data class Error(val message: String) : AuthState() // Estado cuando ocurrió un error, con mensaje incluido.
}

// ViewModel que maneja toda la lógica de autenticación en TitanCake.
class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // Estado interno de autenticación que puede ser observado desde la UI.
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUid = MutableStateFlow<String?>(null)
    val currentUid: StateFlow<String?> = _currentUid.asStateFlow()

    init {
        // Escuchar cambios de sesión desde DataStore
        viewModelScope.launch {
            repository.getUidFlow().collect { uid ->
                _currentUid.value = uid
            }
        }
    }

    // Función para iniciar sesión con correo y contraseña.
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            // Interpretamos el resultado: éxito o error.
            _authState.value = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = {
                    val errorCode = (it as? FirebaseAuthException)?.errorCode
                    val messageAux2 = when (errorCode) {
                        "ERROR_INVALID_EMAIL" -> "El correo es inválido o está mal escrito"
                        else -> "La contraseña o el correo es incorrecto"
                    }
                    println("Login error: $messageAux2")
                    AuthState.Error(messageAux2)
                }
            )

        }
    }

    // Función para registrar un nuevo usuario.
    fun register(email: String, password: String, name: String, confirmpass: String) {
        viewModelScope.launch {
            // Validamos que las contraseñas coincidan antes de continuar.
            if (password != confirmpass) {
                _authState.value = AuthState.Error("Las contraseñas deben coincidir")
                return@launch
            }

            _authState.value = AuthState.Loading
            val result = repository.register(email, password, name)
            // Interpretamos el resultado: éxito o error.
            _authState.value = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = {
                    val errorCode = (it as? FirebaseAuthException)?.errorCode
                    var messageRegAux2 = when (errorCode){
                        "ERROR_INVALID_EMAIL" -> "El correo es invalido o esta mal escrito"
                        "ERROR_EMAIL_ALREADY_IN_USE" -> "Este email ya existe"
                        else -> "Ha ocurrido un error"
                    }
                    println(messageRegAux2)
                    AuthState.Error(messageRegAux2) }
            )
        }
    }

    // Función para cerrar sesión.
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }
}
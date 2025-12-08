package com.example.titancake.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titancake.data.model.Usuario
import com.example.titancake.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Esta clase representa los distintos estados posibles durante el proceso de autenticación.
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()

    data class Success(val uid: String) : AuthState()

    data class Error(val message: String) : AuthState()
}

// ViewModel que maneja toda la lógica de autenticación en TitanCake.
class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)

    var isAdmin by mutableStateOf(false)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUid = MutableStateFlow<String?>(null)
    val currentUid: StateFlow<String?> = _currentUid.asStateFlow()

    val currentUserBackend: Usuario?
        get() = repository.currentUserBackend

    init {
        viewModelScope.launch {
            repository.getUidFlow().collect { uid ->
                _currentUid.value = uid
            }
        }
    }

    fun login(email: String, password: String, esLoginAdmin: Boolean) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)

            result.fold(
                onSuccess = { uid ->
                    // Login exitoso en Firebase y Backend. Ahora validamos el ROL.
                    val usuario = repository.currentUserBackend

                    if (usuario != null) {
                        val rolId = usuario.rol.id

                        // CASO 1: Intenta entrar como Admin (esLoginAdmin = true) pero su rol no es 1
                        if (esLoginAdmin && rolId != 1) {
                            repository.logout() // Lo sacamos inmediatamente
                            _authState.value = AuthState.Error("Esta cuenta no tiene permisos de Administrador.")
                        }
                        // CASO 2: Intenta entrar como Cliente (esLoginAdmin = false) pero es Admin (rol 1)
                        else if (!esLoginAdmin && rolId == 1) {
                            repository.logout()
                            _authState.value = AuthState.Error("Eres Administrador, por favor inicia como tal.")
                        }
                        // CASO 3: Todo coincide
                        else {
                            // Actualizamos la variable para la navegación
                            isAdmin = (rolId == 1)
                            _authState.value = AuthState.Success(uid)
                        }
                    } else {
                        // Caso raro: Firebase ok, pero backend falló silenciosamente
                        _authState.value = AuthState.Error("Error obteniendo datos del usuario.")
                    }
                },
                onFailure = {
                    val errorCode = (it as? FirebaseAuthException)?.errorCode
                    val messageAux2 = when (errorCode) {
                        "ERROR_INVALID_EMAIL" -> "El correo es inválido o está mal escrito"
                        else -> "La contraseña o el correo es incorrecto"
                    }
                    _authState.value = AuthState.Error(messageAux2)
                }
            )
        }
    }

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

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }
}
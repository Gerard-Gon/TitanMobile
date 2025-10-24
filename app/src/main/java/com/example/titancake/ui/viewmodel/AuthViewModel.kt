package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titancake.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
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

    fun register(email: String, password: String, name: String, confirmpass: String) {
        viewModelScope.launch {

            if (password != confirmpass) {
                _authState.value = AuthState.Error("Las contraseñas deben coincidir")
                return@launch
            }

            _authState.value = AuthState.Loading
            val result = repository.register(email, password, name)
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
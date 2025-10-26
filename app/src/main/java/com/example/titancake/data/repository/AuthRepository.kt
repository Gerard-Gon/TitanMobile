package com.example.titancake.data.repository

import android.content.Context
import com.example.titancake.data.local.SessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(context: Context) {

    // Esta es la herramienta que usamos para conectarnos con Firebase.
    private val auth = FirebaseAuth.getInstance()

    // Usamos sesion manager para guardar o borrar el UID del usuario.
    private val sessionManager = SessionManager(context)


    // Esta función permite que un usuario inicie sesión con su correo y contraseña.
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            // Intentamos iniciar sesión con Firebase
            val result = auth.signInWithEmailAndPassword(email, password).await()
            // Obtenemos el UID del usuario (su identificador único)
            val uid = result.user?.uid ?: return Result.failure(Exception("No se pudo obtener UID"))
            // Guardamos el UID en la sesión
            sessionManager.saveUid(uid)
            // Devolvemos el UID como resultado exitoso
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Esta función permite registrar a un nuevo usuario con su correo, contraseña y nombre.
    suspend fun register(email: String, password: String, name: String): Result<String> {
        return try {
            // Creamos el usuario en Firebase.
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            // Obtenemos su UID.
            val uid = result.user?.uid ?: return Result.failure(Exception("No se pudo obtener UID"))
            // Guardamos el UID en la sesión.
            sessionManager.saveUid(uid)
            // Devolvemos el UID como resultado exitoso.
            Result.success(uid)
        } catch (e: Exception) {
            // Si algo falla, devolvemos el error.
            Result.failure(e)
        }
    }

    // Esta función cierra la sesión del usuario.
    suspend fun logout() {
        auth.signOut()
        sessionManager.clearSession()
    }

    // Esta función nos permite observar el UID del usuario en tiempo real.
    // Si cambia (por ejemplo, si inicia o cierra sesión), podemos reaccionar.
    fun getUidFlow() = sessionManager.userUidFlow
}
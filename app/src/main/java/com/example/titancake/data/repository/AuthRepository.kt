package com.example.titancake.data.repository

import android.content.Context
import com.example.titancake.data.local.SessionManager
import com.example.titancake.data.model.Usuario
import com.example.titancake.data.model.UsuarioRequest
import com.example.titancake.data.model.UsuarioRol
import com.example.titancake.data.remote.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(context: Context) {

    private val auth = FirebaseAuth.getInstance()
    private val sessionManager = SessionManager(context)
    private val api = RetrofitInstance.api

    // Variable para guardar el usuario completo del backend en memoria
    var currentUserBackend: Usuario? = null

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            // 1. Login en Firebase
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Error UID Firebase"))

            // 2. Buscar usuario en Backend PostgreSQL para obtener su ID numérico
            val usuariosApi = api.getUsuarios()
            val usuarioBackend = usuariosApi.find { it.correo == email }

            if (usuarioBackend != null) {
                currentUserBackend = usuarioBackend
                // Guardamos el ID del backend también si es necesario, por ahora el UID de firebase
                sessionManager.saveUid(uid)
                Result.success(uid)
            } else {
                Result.failure(Exception("Usuario no encontrado en base de datos TitanCake"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, name: String): Result<String> {
        return try {
            // 1. Crear en Firebase
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Error UID"))

            // 2. Crear en Backend API
            // Asumimos Rol 2 para clientes (ajusta el ID según tu DB: 1 Admin, 2 User)
            val nuevoUsuario = UsuarioRequest(
                nombre = name,
                correo = email,
                contrasena = password, // Enviamos la pass para que el backend la guarde (hasheada allá)
                rol = UsuarioRol(2) // Ajusta este ID según tu tabla de roles
            )

            val response = api.addUsuario(nuevoUsuario)

            if (response.isSuccessful) {
                sessionManager.saveUid(uid)
                Result.success(uid)
            } else {
                // Si falla el backend, deberíamos borrar el de firebase (rollback manual),
                // pero por simplicidad retornamos error.
                Result.failure(Exception("Error creando usuario en Backend: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        auth.signOut()
        sessionManager.clearSession()
        currentUserBackend = null
    }

    fun getUidFlow() = sessionManager.userUidFlow
}
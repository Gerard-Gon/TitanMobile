package com.example.titancake.data.local
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore("user_prefs")

// Esta clase se encarga de manejar la sesión del usuario.
class SessionManager(private val context: Context) {
    companion object {
        // Esta es la "etiqueta" que usaremos para guardar el UID del usuario
        private val USER_UID = stringPreferencesKey("user_uid")
    }
    // Esta funcion nos permite observar el UID del usuario.
    val userUidFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_UID]
    }
    // Esta funcion guarda el UiD del usuario.
    suspend fun saveUid(uid: String) {
        context.dataStore.edit { prefs -> prefs[USER_UID] = uid }
    }
    // Esta funcion borra el UID del usuario.
    suspend fun clearSession() {
        context.dataStore.edit { prefs -> prefs.remove(USER_UID) }
    }
}
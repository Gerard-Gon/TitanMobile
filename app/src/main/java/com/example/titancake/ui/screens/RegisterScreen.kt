package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.titancake.ui.components.ButtonModificado
import com.example.titancake.ui.components.TextFieldModificado
import com.example.titancake.ui.viewmodel.AuthState
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Esta pantalla permite al usuario crear una cuenta en TitanCake.
fun RegisterScreen(
    onRegister: (String, String, String, String) -> Unit,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    authViewModel: AuthViewModel

) {
    // Variables para guardar lo que el usuario escribe.
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Variable para mostrar errores locales (como contraseñas que no coinciden).
    var localError by remember { mutableStateOf<String?>(null) }

    // Observamos el estado de autenticación.
    val authState by authViewModel.authState.collectAsState()

    // Si el estado cambia a éxito, ejecutamos la función de éxito
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onSuccess()
        }
    }

    // Estructura principal de la pantalla con barra superior.
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear Cuenta",
                        fontWeight = FontWeight.Bold,
                        color = BrownP,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = BeigeP)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BeigeP // <-- fondo negro
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize().background(BeigeP)
                .padding(padding),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(8.dp))

                // Campo para escribir el correo.
                TextFieldModificado(email, { email = it }, false, "Correo")


                Spacer(modifier = Modifier.height(8.dp))

                // Campo para escribir la contraseña.
                TextFieldModificado(password, { password = it }, true, "Contraseña")


                Spacer(modifier = Modifier.height(8.dp))

                // Campo para confirmar la contraseña.
                TextFieldModificado(confirmPassword, { confirmPassword = it }, true, "Confirmar contraseña")


                Spacer(modifier = Modifier.height(16.dp))

                // Botón para registrarse.
                ButtonModificado("Registrarse", {
                    if (password != confirmPassword) {
                        localError = "Las contraseñas deben coincidir"
                    } else {
                        localError = null
                        onRegister(email.trim(), password.trim(), name.trim(), confirmPassword.trim())
                    }
                })

                // Si hay un error local, lo mostramos.
                localError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }



                Spacer(modifier = Modifier.height(16.dp))

                // Mostramos un indicador de carga o error según el estado de autenticación.
                when (authState) {
                    is AuthState.Loading -> CircularProgressIndicator()
                    is AuthState.Error -> Text(
                        text = (authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    else -> {}
                }
            }
        }
    }
}


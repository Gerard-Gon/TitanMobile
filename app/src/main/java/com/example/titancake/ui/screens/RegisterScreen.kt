package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = BrownP)
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
                .fillMaxSize()
                .background(BeigeP)
                .padding(padding)
                .verticalScroll(rememberScrollState()), // Permite scroll si el teclado tapa
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // --- CORRECCIÓN: AGREGADO CAMPO NOMBRE ---
            TextFieldModificado(
                newValue = name,
                onChange = { name = it },
                isPassword = false,
                label = "Nombre Completo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldModificado(
                newValue = email,
                onChange = { email = it },
                isPassword = false,
                label = "Correo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldModificado(
                newValue = password,
                onChange = { password = it },
                isPassword = true,
                label = "Contraseña"
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldModificado(
                newValue = confirmPassword,
                onChange = { confirmPassword = it },
                isPassword = true,
                label = "Confirmar contraseña"
            )

            Spacer(modifier = Modifier.height(24.dp))

            ButtonModificado(text = "Registrarse", onClick = {
                // --- CORRECCIÓN: VALIDACIONES ---
                when {
                    name.isBlank() -> localError = "El nombre no puede estar vacío"
                    email.isBlank() -> localError = "El correo es obligatorio"
                    password.length < 6 -> localError = "La contraseña debe tener al menos 6 caracteres"
                    password != confirmPassword -> localError = "Las contraseñas deben coincidir"
                    else -> {
                        localError = null
                        onRegister(email.trim(), password.trim(), name.trim(), confirmPassword.trim())
                    }
                }
            })

            localError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Manejo de errores que vienen del servidor/firebase
            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = BrownP, modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}


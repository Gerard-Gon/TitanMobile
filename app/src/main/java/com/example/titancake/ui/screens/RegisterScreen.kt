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
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    authViewModel: AuthViewModel

) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear Cuenta",
                        fontWeight = FontWeight.Bold,
                        color = BeigeP,
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
                    containerColor = BrownP // <-- fondo negro
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize().background(BrownP)
                .padding(padding),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                TextFieldModificado(name, { name = it }, false, "Ingrese su nombre completo")


                Spacer(modifier = Modifier.height(8.dp))

                TextFieldModificado(direccion, { direccion = it }, false, "Ingrese su direccion")


                Spacer(modifier = Modifier.height(8.dp))

                TextFieldModificado(email, { email = it }, false, "Correo")


                Spacer(modifier = Modifier.height(8.dp))


                TextFieldModificado(password, { password = it }, true, "Contraseña")


                Spacer(modifier = Modifier.height(8.dp))


                TextFieldModificado(confirmPassword, { confirmPassword = it }, true, "Confirmar contraseña")


                Spacer(modifier = Modifier.height(16.dp))

                ButtonModificado("Registrarse", {
                    if (password == confirmPassword) {
                        onRegister(email.trim(), password.trim(), name.trim())
                    }
                })

                Spacer(modifier = Modifier.height(16.dp))

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


package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.titancake.ui.components.ButtonModificado
import com.example.titancake.ui.components.TextFieldModificado
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.Black
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.viewmodel.AuthState
import com.example.titancake.ui.viewmodel.AuthViewModel



@Composable
// Esta pantalla permite al usuario iniciar sesión en TitanCake.
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onSuccessCliente: () -> Unit,
    onSuccessAdmin: () -> Unit,
    authViewModel: AuthViewModel,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    var adminLogin by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            if (adminLogin) onSuccessAdmin() else onSuccessCliente()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BeigeP)
                .padding(padding)
        ) {
            Text(
                text = "Inicio sesión",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = BrownP,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(50.dp))

            TextFieldModificado(email, { email = it }, false, "Correo")
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldModificado(password, { password = it }, true, "Contraseña")
            Spacer(modifier = Modifier.height(16.dp))

            ButtonModificado(
                text = "Ingresar como cliente",
                onClick = {
                    if (password.isBlank()) {
                        localError = "La contraseña no puede estar vacía"
                    } else {
                        localError = null
                        adminLogin = false
                        authViewModel.isAdmin = false
                        onLogin(email, password)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonModificado(
                text = "Ingresar como Administrador",
                onClick = {
                    if (password.isBlank()) {
                        localError = "La contraseña no puede estar vacía"
                    } else {
                        localError = null
                        adminLogin = true
                        authViewModel.isAdmin = true
                        onLogin(email, password)
                    }
                }
            )

            localError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

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

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Regístrate",
                    color = Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
package com.example.titancake.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.theme.PurpleGrey40
import com.example.titancake.ui.viewmodel.AuthViewModel

@Composable
// Esta pantalla muestra el perfil del usuario en TitanCake.
fun ProfileScreen(authViewModel: AuthViewModel, navControllerApp: NavHostController) {
    // Observamos el estado de autenticación (por si el usuario cierra sesión).
    val authState by authViewModel.authState.collectAsState()

    // Variable que guarda la imagen seleccionada por el usuario.
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador para abrir la galería y seleccionar una imagen.
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri // Guardamos la imagen seleccionada.
    }

    // Estructura principal de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeP)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla.
        Text(
            text = "Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BrownP
        )

        Spacer(Modifier.height(32.dp))

        // Si el usuario ya seleccionó una imagen, la mostramos dentro de una tarjeta.
        if (imageUri != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(300.dp),
                colors = CardDefaults.cardColors(containerColor = PurpleGrey40)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Recortamos la imagen para que se vea bien.

                )
            }

            Spacer(Modifier.height(16.dp))
        }

        // Botón para seleccionar una imagen desde la galería.
        Button(
            onClick = { selectImageLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = BrownP),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregue una imagen de perfil aqui", color = BeigeP)
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { navControllerApp.navigate("get") },
            colors = ButtonDefaults.buttonColors(containerColor = BrownP),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manejo de Productos", color = BeigeP)
        }

        Spacer(Modifier.height(32.dp))


        // Botón para cerrar sesión.
        Button(
            onClick = {
                try {
                    authViewModel.logout()
                    navControllerApp.navigate("login") {
                        popUpTo("home") { inclusive = true } // Volvemos al login y limpiamos el historial.
                    }
                } catch (err: Exception) {
                    // Si algo falla, lo ignoramos por ahora.
                }

            },
            colors = ButtonDefaults.buttonColors(containerColor = PurpleGrey40),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión", color = BeigeP)
        }
    }
}
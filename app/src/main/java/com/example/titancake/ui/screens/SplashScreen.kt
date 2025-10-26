package com.example.titancake.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.titancake.ui.theme.BrownP
import kotlinx.coroutines.delay
import com.example.titancake.R
import com.example.titancake.ui.theme.White


@Composable
// Esta pantalla se muestra brevemente al iniciar la app.
fun SplashScreen(onTimeout: () -> Unit) {
    // Usamos LaunchedEffect para esperar 2 segundos y luego ejecutar la acción de salida
    LaunchedEffect(true) {
        delay(2000)
        onTimeout()
    }

    // Caja principal que ocupa toda la pantalla.
    Box(modifier = Modifier.fillMaxSize()) {
        // Mostramos el logo de la app ocupando.
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.FillBounds, // La imagen se ajusta al tamaño completo.
            modifier = Modifier.fillMaxSize()
        )

        // Capa semitransparente encima del logo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BrownP.copy(alpha = 0.5f))
        )

        // Indicador de carga centrado en la pantalla.
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = White)
        }
    }
}
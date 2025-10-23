package com.example.titancake.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import kotlinx.coroutines.delay
import com.example.titancake.R
import com.example.titancake.ui.theme.White


@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(true) {
        delay(5000)
        onTimeout()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // Capa semitransparente opcional
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BrownP.copy(alpha = 0.5f))
        )

        // Loader centrado
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = White)
        }
    }
}
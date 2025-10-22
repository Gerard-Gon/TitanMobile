package com.example.titancake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Simple: muestra splash y despues llama onTimeout
    // En Compose puro, preferirías LaunchedEffect { delay(1000L); onTimeout() }
    LaunchedEffect(true) {
        delay(1000)
        onTimeout()
    }

    Box(modifier = Modifier.fillMaxSize().background(BrownP), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = BeigeP)
    }
}
package com.example.titancake.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.titancake.ui.theme.Green
import com.example.titancake.ui.theme.White

@Composable
// Este es un botón personalizado que se puede usar en distintas partes de la app TitanCake.
fun ButtonModificado(
    text: String = "Botón", // Texto que se muestra dentro del botón
    onClick: () -> Unit,    // Acción que se ejecuta cuando el usuario toca el botón.
    enabled: Boolean = true // Si está en false, el botón aparece apagado y no se puede tocar.
) {
    Button(
        onClick = onClick,  // Aquí definimos qué pasa cuando el botón se toca.
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),  // Tiene una altura fija de 56dp
        colors = ButtonDefaults.buttonColors(
            containerColor = Green,  // Color principal del botón
            disabledContainerColor = Green.copy(alpha = 0.5f) // Color cuando está desactivado
        ),
        shape = RoundedCornerShape(28.dp), // Bordes redondeados como si fuera un pastelito suave.
        enabled = enabled  // Activamos o desactivamos el botón según el parámetro.
    ) {
        Text(
            text = text,  // Mostramos el texto que recibimos como parámetro.
            color = White,  // El texto es blanco para que contraste con el fondo verde.
            fontSize = 18.sp,  // Tamaño de letra grande y legible.
            fontWeight = FontWeight.Bold  // Letras en negrita para que se vea con fuerza.
        )
    }
}
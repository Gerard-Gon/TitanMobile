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
fun ButtonModificado(
    text: String = "Botón",
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Green,
            disabledContainerColor = Green.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(28.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
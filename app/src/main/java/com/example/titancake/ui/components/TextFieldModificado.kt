package com.example.titancake.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.Black
import com.example.titancake.ui.theme.Green


@Composable
// Este es un campo de texto personalizado que se adapta según si es para escribir una contraseña o un dato normal.
fun TextFieldModificado(newValue: String, onChange: (String) -> Unit, isPassword: Boolean, label: String) {
    // Esta variable recuerda si la contraseña está visible o no.
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = newValue, // Mostramos el valor actual del campo.
        onValueChange = onChange,  // Actualizamos el valor cuando el usuario escribe.
        // Este es el texto guía que aparece cuando el campo está vacío.
        placeholder = {
            Text(
                label,
                color =  Black.copy(alpha = 0.9f) // Color ligeramente transparente para que se vea suave.
            )
        },
        // Si el campo es de contraseña, mostramos un ícono para mostrar/ocultar el texto.
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Favorite else Icons.Default.Lock,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = BeigeP // Color personalizado para el ícono.
                    )
                }
            }

        },
        // Si es contraseña, ocultamos el texto con puntitos. Si no, lo mostramos normalmente.
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        // Estilo del campo.
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        // Colores personalizados para bordes, texto y cursor.
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Black.copy(alpha = 0.5f),
            unfocusedBorderColor = Black.copy(alpha = 0.3f),
            focusedTextColor = Black,
            unfocusedTextColor = Black,
            cursorColor = Green,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(30.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true // Solo permite una línea de texto.
    )
}
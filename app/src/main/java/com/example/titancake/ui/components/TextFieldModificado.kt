package com.example.titancake.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import com.example.titancake.ui.theme.Green
import com.example.titancake.ui.theme.LightGray
import com.example.titancake.ui.theme.PurpleGrey40
import com.example.titancake.ui.theme.White


@Composable
fun TextFieldModificado(newValue: String, onChange: (String) -> Unit, isPassword: Boolean, label: String) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = newValue,
        onValueChange = onChange,
        placeholder = {
            Text(
                label,
                color =  White.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Favorite else Icons.Default.Favorite,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = White
                    )
                }
            }

        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LightGray.copy(alpha = 0.5f),
            unfocusedBorderColor = LightGray.copy(alpha = 0.3f),
            focusedTextColor = White,
            unfocusedTextColor = White,
            cursorColor = Green,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(30.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true
    )
}
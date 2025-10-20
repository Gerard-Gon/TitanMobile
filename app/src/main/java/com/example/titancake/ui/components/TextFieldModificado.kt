package com.example.titancake.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextFieldModificado(value: String,
                        onChange:(String)-> Unit,
                        error: String? = null,
                        label: String){
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = {
            Text(label)
        }
    )


}
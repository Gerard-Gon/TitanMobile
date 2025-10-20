package com.example.titancake.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.titancake.data.model.AddFormState

class AddViewModel : ViewModel() {

    var formState = mutableStateOf(AddFormState())

    fun onChangeName(newValue:String){
        formState.value = formState.value.copy(
            name = newValue,
            nameError = when{
                newValue.isBlank()-> "Nombre Vacio"
                else -> null
            }
        )
    }

}
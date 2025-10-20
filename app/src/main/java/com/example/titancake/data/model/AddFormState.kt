package com.example.titancake.data.model

data class AddFormState (
    val name: String = "",
    val description : String = "",

    //Errores
    val nameError : String? = null,
    val  descriptionError: String? = null
)
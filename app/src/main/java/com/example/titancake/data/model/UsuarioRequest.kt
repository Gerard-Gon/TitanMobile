package com.example.titancake.data.model

data class UsuarioRequest (
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: UsuarioRol,
    val authFireBase: String
)
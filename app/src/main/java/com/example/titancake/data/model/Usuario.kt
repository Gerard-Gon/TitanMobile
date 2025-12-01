package com.example.titancake.data.model

data class Usuario (
    val id: Int,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: UsuarioRol
)

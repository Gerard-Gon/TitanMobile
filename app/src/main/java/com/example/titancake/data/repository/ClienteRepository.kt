package com.example.titancake.data.repository

import com.example.titancake.data.model.Usuario
import com.example.titancake.ui.components.usuarios

class ClienteRepository {

    private val user = usuarios

    fun getAll(): List<Usuario> = user

    fun getById(id: Int): Usuario? = user.find { it.id == id }

}
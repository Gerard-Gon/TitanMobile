package com.example.titancake.data.repository

import com.example.titancake.data.model.Usuario
import com.example.titancake.data.model.UsuarioRequest
import com.example.titancake.data.remote.RetrofitInstance
import retrofit2.Response

interface UsuarioRepositoryInterface {
    suspend fun getUsuario(): List<Usuario>
    suspend fun addUsuario(usuario: UsuarioRequest): Response<Usuario>
    suspend fun deleteUsuario(id: Int): Response<Unit>
    suspend fun updateUsuario(id: Int, usuario: Usuario): Response<Usuario>
}


class UsuarioRepository : UsuarioRepositoryInterface{

    override suspend fun getUsuario(): List<Usuario> {
        return RetrofitInstance.api.getUsuarios()
    }

    override suspend fun addUsuario(usuario: UsuarioRequest): Response<Usuario> {
        return RetrofitInstance.api.addUsuario(usuario)
    }

    override suspend fun deleteUsuario(id: Int): Response<Unit> {
        return RetrofitInstance.api.deleteUsuario(id)
    }

    override suspend fun updateUsuario(id: Int, usuario: Usuario): Response<Usuario> {
        return RetrofitInstance.api.updateUsuario(id, usuario)
    }


}
package com.example.titancake.data.remote

import com.example.titancake.data.model.Post
import retrofit2.http.GET

// Esta interfaz define los endpoints HTTP
interface ApiService {

    // Define una solicitud GET al endpoint /posts
    @GET("/posts")
    suspend fun getPosts(): List<Post>
}

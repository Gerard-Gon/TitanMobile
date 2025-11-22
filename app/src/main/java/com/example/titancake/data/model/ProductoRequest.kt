package com.example.titancake.data.model

data class ProductoRequest(
    val nombreProducto: String,
    val precio: Int,
    val descripcionProducto: String,
    val stock: Int,
    val imageUrl: String,
    val categoria: CategoriaRequest
)
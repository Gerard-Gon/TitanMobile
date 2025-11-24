package com.example.titancake.data.model

data class Producto(
    val id: Int,
    val nombreProducto: String,
    val precio: Int,
    val descripcionProducto: String,
    val stock: Int,
    val imageUrl: String,
    val categoria: Categoria
)

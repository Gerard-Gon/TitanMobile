package com.example.titancake.data.model

// Modelo que representa un ítem tal cual viene del Backend (/api/v1/itemscarrito)
data class ItemCarritoBackend(
    val id: Int,
    val cantidad: Int,
    val precioUnitario: Int,
    val producto: Producto, // El backend devuelve el objeto producto completo
    val carrito: CarritoBackend // El backend devuelve el objeto carrito completo
)

// Modelo simplificado del Carrito que viene dentro del Item
data class CarritoBackend(
    val id: Int,
    val usuario: Usuario? // Puede ser null si el usuario se borró
)

// Clase auxiliar para agrupar los datos en la pantalla de Admin
data class VentaAgrupada(
    val carritoId: Int,
    val nombreUsuario: String,
    val items: List<ItemCarritoBackend>,
    val totalVenta: Int
)
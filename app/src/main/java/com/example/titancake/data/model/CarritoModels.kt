package com.example.titancake.data.model

// Para enviar al backend al crear un carrito
data class CarritoRequest(
    val usuario: UsuarioId // Enviamos el objeto con solo el ID del usuario
)

data class UsuarioId(val id: Int)
data class ProductoId(val id: Int)
data class CarritoId(val id: Int)

// Respuesta del backend al crear carrito
data class CarritoResponse(
    val id: Int,
    val usuario: Usuario
)

// Para enviar items al backend
data class ItemCarritoRequest(
    val cantidad: Int,
    val precioUnitario: Int,
    val carrito: CarritoId,
    val producto: ProductoId
)

data class ItemCarritoResponse(
    val id: Int,
    val cantidad: Int
    // ... otros campos si son necesarios
)
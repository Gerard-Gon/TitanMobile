package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.titancake.data.model.ItemCarrito
import com.example.titancake.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ViewModel que maneja la lógica del carrito de compras en TitanCake.
class CartViewModel : ViewModel() {

    // Estado interno del carrito: una lista de productos con su cantidad.
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()


    // Agrega un producto al carrito.
    // Si ya existe, suma la cantidad. Si no, lo agrega como nuevo.
    fun agregarProducto(producto: Producto, cantidad: Int) {
        val actualizado = _carrito.value.map {
            if (it.producto.id == producto.id) {
                it.copy(cantidad = it.cantidad + cantidad) // Aumentamos la cantidad si ya existe.
            } else it
        }

        if (_carrito.value.any { it.producto.id == producto.id }) {
            _carrito.value = actualizado
        } else {
            _carrito.value = _carrito.value + ItemCarrito(producto, cantidad)
        }
    }


    // Quita una unidad del producto con el ID dado.
    // Si la cantidad llega a 0, lo elimina del carrito.
    fun quitarUnidad(id: Int) {
        _carrito.value = _carrito.value.mapNotNull {
            if (it.producto.id == id) {
                if (it.cantidad > 1) it.copy(cantidad = it.cantidad - 1)
                else null
            } else it
        }
    }

    // Elimina completamente un producto del carrito según su ID.
    fun eliminarProducto(id: Int) {
        _carrito.value = _carrito.value.filterNot { it.producto.id == id }
    }

    // Vacía el carrito.
    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }

    // Calcula el total a pagar sumando el precio por cantidad de cada producto.
    fun total(): Double = _carrito.value.sumOf { it.producto.precio * it.cantidad }





}


package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.titancake.data.model.ItemCarrito
import com.example.titancake.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()


    fun agregarProducto(producto: Producto, cantidad: Int) {
        val existente = _carrito.value.find { it.producto.id == producto.id }
        if (existente != null) {
            existente.cantidad += cantidad
            _carrito.value = _carrito.value.toList()
        } else {
            _carrito.value = _carrito.value + ItemCarrito(producto, cantidad)
        }
    }

    fun quitarUnidad(id: Int) {
        _carrito.value = _carrito.value.mapNotNull {
            if (it.producto.id == id) {
                if (it.cantidad > 1) it.copy(cantidad = it.cantidad - 1)
                else null
            } else it
        }
    }

    fun eliminarProducto(id: Int) {
        _carrito.value = _carrito.value.filterNot { it.producto.id == id }
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }

    fun total(): Double = _carrito.value.sumOf { it.producto.precio * it.cantidad }

}
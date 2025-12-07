package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titancake.data.model.CarritoId
import com.example.titancake.data.model.CarritoRequest
import com.example.titancake.data.model.ItemCarrito
import com.example.titancake.data.model.ItemCarritoRequest
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoId
import com.example.titancake.data.model.UsuarioId
import com.example.titancake.data.remote.RetrofitInstance
import com.example.titancake.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel que maneja la lógica del carrito de compras en TitanCake.
class CartViewModel(private val authRepository: AuthRepository) : ViewModel() {

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
    fun total(): Int = _carrito.value.sumOf { it.producto.precio * it.cantidad }

    fun confirmarCompra(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val usuario = authRepository.currentUserBackend
        val itemsActuales = _carrito.value

        if (usuario == null) {
            onError("Usuario no identificado. Reinicie sesión.")
            return
        }
        if (itemsActuales.isEmpty()) {
            onError("El carrito está vacío.")
            return
        }

        viewModelScope.launch {
            try {
                val api = RetrofitInstance.api

                // 1. Crear el Carrito (Cabecera)
                val carritoReq = CarritoRequest(usuario = UsuarioId(usuario.id))
                val respCarrito = api.createCarrito(carritoReq)

                if (!respCarrito.isSuccessful || respCarrito.body() == null) {
                    onError("Error al iniciar la compra")
                    return@launch
                }

                val carritoId = respCarrito.body()!!.id

                // 2. Iterar items para Guardar Detalle y Actualizar Stock
                itemsActuales.forEach { itemLocal ->
                    // A. Guardar Item en Backend
                    val itemReq = ItemCarritoRequest(
                        cantidad = itemLocal.cantidad,
                        precioUnitario = itemLocal.producto.precio,
                        carrito = CarritoId(carritoId),
                        producto = ProductoId(itemLocal.producto.id)
                    )
                    api.createItemCarrito(itemReq)

                    // B. Actualizar Stock (Reducir)
                    val nuevoStock = itemLocal.producto.stock - itemLocal.cantidad
                    // Creamos una copia del producto con el stock actualizado
                    val productoActualizado = itemLocal.producto.copy(stock = if (nuevoStock < 0) 0 else nuevoStock)

                    api.updateProducto(itemLocal.producto.id, productoActualizado)
                }

                // 3. Finalizar
                vaciarCarrito()
                onSuccess()

            } catch (e: Exception) {
                onError("Error de conexión: ${e.message}")
            }
        }
    }

}


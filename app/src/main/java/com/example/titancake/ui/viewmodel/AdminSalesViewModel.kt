package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titancake.data.model.ItemCarritoBackend
import com.example.titancake.data.model.VentaAgrupada
import com.example.titancake.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminSalesViewModel : ViewModel() {

    private val api = RetrofitInstance.api

    // Lista de ventas ya agrupadas por carrito
    private val _ventas = MutableStateFlow<List<VentaAgrupada>>(emptyList())
    val ventas: StateFlow<List<VentaAgrupada>> = _ventas.asStateFlow()

    // Monto total de todo lo vendido en la historia
    private val _totalHistorico = MutableStateFlow(0)
    val totalHistorico: StateFlow<Int> = _totalHistorico.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun cargarVentas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Obtenemos TODOS los items
                val todosLosItems = api.getAllVentas()

                // 2. Calculamos Total Histórico
                // CAMBIO: Usamos 'item ->' en vez de 'it'
                val total = todosLosItems.sumOf { item ->
                    item.cantidad * item.precioUnitario
                }
                _totalHistorico.value = total

                // 3. Agrupamos y Mapeamos
                val agrupadas = todosLosItems
                    .groupBy { item -> item.carrito.id } // Agrupar por ID
                    .map { (carritoId, listaDeItems) -> // Desestructuración explícita

                        // Obtenemos el nombre del primer ítem (si existe)
                        val usuarioNombre = listaDeItems.firstOrNull()
                            ?.carrito?.usuario?.nombre
                            ?: "Usuario Desconocido"

                        // Calculamos el total de ESTA venta específica
                        // CAMBIO: Usamos 'itemVenta ->' para evitar confusión
                        val totalDeEstaVenta = listaDeItems.sumOf { itemVenta ->
                            itemVenta.cantidad * itemVenta.precioUnitario
                        }

                        VentaAgrupada(
                            carritoId = carritoId,
                            nombreUsuario = usuarioNombre,
                            items = listaDeItems,
                            totalVenta = totalDeEstaVenta
                        )
                    }
                    // CAMBIO: Usamos 'venta ->' para ordenar
                    .sortedByDescending { venta -> venta.carritoId }

                _ventas.value = agrupadas

            } catch (e: Exception) {
                println("Error cargando ventas: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}
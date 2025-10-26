package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titancake.data.model.Producto
import com.example.titancake.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel principal que maneja la lógica de productos en TitanCake.
class MainViewModel(
    private val repo: ProductoRepository = ProductoRepository()
) : ViewModel() {

    // Estado interno que guarda la lista de productos disponibles.
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())

    // Exponemos los productos como un flujo de solo lectura para que la UI pueda observarlos.
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        // Al iniciar el ViewModel, lanzamos una corrutina para cargar todos los productos desde el repositorio.
        viewModelScope.launch {
            _productos.value = repo.getAll()
        }
    }

    // Función para obtener un producto específico por su ID.
    fun getProducto(id: Int): Producto? = repo.getById(id)
}


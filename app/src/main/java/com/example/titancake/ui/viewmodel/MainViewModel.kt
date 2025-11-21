package com.example.titancake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import com.example.titancake.data.remote.RetrofitInstance
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

    private val apiService = RetrofitInstance.api

    protected val _productos = MutableStateFlow<List<Producto>>(emptyList())

    open val productosList: StateFlow<List<Producto>> = _productos


    // Función para obtener un producto específico por su ID.

    fun fetchProductos() {
        viewModelScope.launch {
            try {
                val result = repo.getProductos()
                println("Productos recibidos: ${result.size}")
                _productos.value = result
            } catch (e: Exception) {
                println("Error al obtener los datos: ${e.localizedMessage}")
            }
        }
    }

    fun getProducto(itemId: Int): Producto? {
        return productosList.value.find { it.id == itemId }
    }

    suspend fun fetchProductoById(id: Int): Producto? {
        return try {
            apiService.getProductoById(id)
        } catch (e: Exception) {
            null // Manejo de error si el producto no se encuentra o hay problema de red
        }
    }

    fun deleteProducto(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.deleteProducto(id)
                if (response.isSuccessful) {
                    // Actualiza la lista local quitando el producto
                    _productos.value = _productos.value.filter { it.id != id }
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun addProducto(producto: ProductoRequest, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.addProducto(producto)
                if (response.isSuccessful) {
                    response.body()?.let { nuevo ->
                        // aquí ya tienes el id generado por la API
                        _productos.value = _productos.value + nuevo
                        println("Producto agregado con id: ${nuevo.id}")
                    }
                    onResult(true)
                } else {
                    println("Error: ${response.code()} - ${response.errorBody()?.string()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                println("Excepción: ${e.localizedMessage}")
                onResult(false)
            }
        }
    }

    fun updateProducto(id: Int, producto: Producto, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.updateProducto(id, producto)
                if (response.isSuccessful) {
                    response.body()?.let { actualizado ->
                        // Reemplaza el producto en la lista
                        _productos.value = _productos.value.map {
                            if (it.id == id) actualizado else it
                        }
                    }
                    onResult(true)
                } else {
                    println("Error: ${response.code()} - ${response.errorBody()?.string()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                println("Excepción: ${e.localizedMessage}")
                onResult(false)
            }
        }
    }






    // Exponemos los productos como un flujo de solo lectura para que la UI pueda observarlos.

    /*
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        // Al iniciar el ViewModel, lanzamos una corrutina para cargar todos los productos desde el repositorio.
        viewModelScope.launch {
            _productos.value = repo.getAll()
        }
    }

    fun getProducto(id: Int): Producto? = repo.getById(id)

     */


}


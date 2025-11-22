package com.example.titancake

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.titancake.data.repository.ProductoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductoRepositoryInstrumentedTest {

    @Test
    fun test_real_ap_response() = runBlocking {

        val repo = ProductoRepository()

        val producto = repo.getProductos()

        assertTrue(producto.isNotEmpty())
        assertNotNull(producto.first().nombreProducto)

    }
}
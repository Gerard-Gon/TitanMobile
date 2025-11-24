package com.example.titancake.ui.screens.admin

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.titancake.data.model.CategoriaRequest
import com.example.titancake.data.model.Producto
import com.example.titancake.data.model.ProductoRequest
import com.example.titancake.data.remote.RetrofitInstance
import com.example.titancake.ui.components.TextFieldModificado
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.viewmodel.AuthViewModel
import com.example.titancake.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.Manifest
import androidx.compose.foundation.verticalScroll

suspend fun uploadProductImage(file: File): String? {
    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    val multipart = MultipartBody.Part.createFormData("file", file.name, requestBody)

    // Usamos apiFiles
    val response = RetrofitInstance.apiFiles.uploadFile(multipart)

    return if (response.isSuccessful) {
        response.body()?.data?.url?.replace("http://", "https://")
    } else {
        println("Error: ${response.errorBody()?.string()}")
        null
    }
}

// Esta pantalla muestra los detalles de un producto específico.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(authViewModel: AuthViewModel,  navControllerApp: NavHostController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope ()
    var producto by remember { mutableStateOf<Producto?>(null) }

    // Estados para cada campo del producto
    var nombreProducto by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcionProducto by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var categoriaIdInput by remember { mutableStateOf("") }
    var localImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isUploading by remember { mutableStateOf(false) } //

    // Launcher Galería
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {

            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            val file = File(context.cacheDir, "product_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            isUploading = true
            coroutineScope.launch {
                val url = uploadProductImage(file)
                if (url != null) {
                    imageUrl = url
                }
                isUploading = false
            }
        }
    }

    // Launcher Cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            localImageBitmap = it
            val file = File(context.cacheDir, "product_photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                it.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            isUploading = true
            coroutineScope.launch {
                val url = uploadProductImage(file)
                if (url != null) {
                    imageUrl = url
                }
                isUploading = false
            }
        }
    }

    // Permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                takePictureLauncher.launch(null)
            }
        }
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BeigeP)
            )
        },
        containerColor = BeigeP
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .background(BeigeP)
                .verticalScroll(androidx.compose.foundation.rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotEmpty()) {
                    // Si ya se subió, mostramos la URL
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Imagen subida",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (localImageBitmap != null) {
                    // Si es una foto local recién tomada
                    Image(
                        bitmap = localImageBitmap!!.asImageBitmap(),
                        contentDescription = "Foto local",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Sin imagen seleccionada", color = BrownP)
                }

                if (isUploading) {
                    CircularProgressIndicator(color = BrownP)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- BOTONES DE SELECCIÓN ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { selectImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = BrownP)
                ) {
                    Text("Galería", color = BeigeP)
                }

                Button(
                    onClick = {
                        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            takePictureLauncher.launch(null)
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrownP)
                ) {
                    Text("Cámara", color = BeigeP)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))



            // Campos de texto existentes
            TextFieldModificado(newValue = nombreProducto, onChange = { nombreProducto = it }, isPassword = false, label = "Nombre del producto")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = precio, onChange = { precio = it }, isPassword = false, label = "Precio")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = descripcionProducto, onChange = { descripcionProducto = it }, isPassword = false, label = "Descripción")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = stock, onChange = { stock = it }, isPassword = false, label = "Stock")
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(newValue = imageUrl, onChange = { imageUrl = it }, isPassword = false, label = "URL de imagen")


            Spacer(modifier = Modifier.height(10.dp))
            TextFieldModificado(
                newValue = categoriaIdInput,
                onChange = { categoriaIdInput = it },
                isPassword = false,
                label = "ID de Categoría (Obligatorio)"
            )

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                    val categoriaId = categoriaIdInput.toIntOrNull()
                    if (categoriaId != null && categoriaId > 0) {
                        // Verificamos que haya una imagen (opcional)
                        if (imageUrl.isEmpty()) {
                            println("Advertencia: No hay imagen seleccionada, se enviará vacía.")
                        }

                        val categoriaRequest = CategoriaRequest(id = categoriaId)
                        val nuevoProducto = ProductoRequest(
                            nombreProducto = nombreProducto,
                            precio = precio.toIntOrNull() ?: 0,
                            descripcionProducto = descripcionProducto,
                            stock = stock.toIntOrNull() ?: 0,
                            imageUrl = imageUrl, // Aquí va la URL que nos devolvió la API al subir la foto
                            categoria = categoriaRequest
                        )
                        viewModel.addProducto(nuevoProducto) { success ->
                            if (success) {
                                navControllerApp.popBackStack()
                            }
                        }
                    } else {
                        println("Error: El ID de Categoría debe ser válido.")
                    }
                },

                enabled = !isUploading,
                colors = ButtonDefaults.buttonColors(containerColor = BrownP),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isUploading) Text("Subiendo imagen...", color = BeigeP)
                else Text("Agregar Producto", color = BeigeP)
            }
        }
    }
}
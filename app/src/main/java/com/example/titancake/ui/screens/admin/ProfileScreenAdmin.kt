package com.example.titancake.ui.screens.admin

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.titancake.data.remote.RetrofitInstance
import com.example.titancake.ui.theme.BeigeP
import com.example.titancake.ui.theme.BrownP
import com.example.titancake.ui.theme.PurpleGrey40
import com.example.titancake.ui.theme.White
import com.example.titancake.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

suspend fun uploadImage(file: File): String? {
    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    val multipart = MultipartBody.Part.createFormData("file", file.name, requestBody)

    val response = RetrofitInstance.apiFiles.uploadFile(multipart)

    return if (response.isSuccessful) {
        response.body()?.data?.url
    } else {
        println("Error: ${response.errorBody()?.string()}")
        null
    }
}

@Composable
// Esta pantalla muestra el perfil del usuario en TitanCake.
fun ProfileScreenAdmin(authViewModel: AuthViewModel, navControllerApp: NavHostController) {
    // Observamos el estado de autenticación (por si el usuario cierra sesión).
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    // Variable que guarda la imagen seleccionada por el usuario.
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var localImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }

    // Lanzador para abrir la galería y seleccionar una imagen.
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri // Guardamos la imagen seleccionada.
    }

    //Launcher para pedir permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                println("Permiso de cámara denegado")
            }
        }
    )

    //Launcher para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            localImageBitmap = it
            val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                it.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            coroutineScope.launch {
                uploadedImageUrl = uploadImage(file)
            }
        }
    }

    //Launcher para elegir desde galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            // Copiar el archivo desde la URI a un archivo temporal
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            val file = File(context.cacheDir, "gallery_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            coroutineScope.launch {
                uploadedImageUrl = uploadImage(file)
            }
        }
    }

    //Pedir permiso de cámara al iniciar
    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    // Estructura principal de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeP)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla.
        Text(
            text = "Perfil de Admin",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BrownP
        )

        Spacer(Modifier.height(32.dp))

        // Si el usuario ya seleccionó una imagen, la mostramos dentro de una tarjeta.
        if (imageUri != null || localImageBitmap != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(300.dp),
                colors = CardDefaults.cardColors(containerColor = PurpleGrey40)
            ) {
                if (localImageBitmap != null) {
                    Image(
                        bitmap = localImageBitmap!!.asImageBitmap(),
                        contentDescription = "Imagen capturada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }


            Spacer(Modifier.height(16.dp))
        }

        Button(
            onClick = { takePictureLauncher.launch(null) },
            colors = ButtonDefaults.buttonColors(containerColor = BrownP),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar Foto", color = BeigeP)
        }

        Spacer(Modifier.height(16.dp))

        // Botón para seleccionar una imagen desde la galería.
        Button(
            onClick = { selectImageLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = BrownP),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Elegir desde Galeria", color = BeigeP)
        }

        Spacer(Modifier.height(16.dp))

        uploadedImageUrl?.let { url ->
            Text(
                text = "Imagen subida correctamente",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BrownP
            )

            AsyncImage(
                model = url,
                contentDescription = url.replace("http://", "https://"),
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = url.replace("http://", "https://"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BrownP
            )

            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.height(16.dp))

        // Botón para cerrar sesión.
        Button(
            onClick = {
                try {
                    authViewModel.logout()
                    navControllerApp.navigate("login") {
                        popUpTo("home") { inclusive = true } // Volvemos al login y limpiamos el historial.
                    }
                } catch (err: Exception) {
                    // Si algo falla, lo ignoramos por ahora.
                }

            },
            colors = ButtonDefaults.buttonColors(containerColor = PurpleGrey40),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión", color = BeigeP)
        }
    }
}
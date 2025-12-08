package com.example.titancake.ui.screens.admin

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.titancake.ui.theme.Black
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

// Función auxiliar para subir imagen (Misma lógica anterior)
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
fun ProfileScreenAdmin(authViewModel: AuthViewModel, navControllerApp: NavHostController) {
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // --- AGREGADO: Obtenemos los datos del administrador ---
    val usuarioBackend = authViewModel.currentUserBackend

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var localImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }

    // Launchers de imagen (Misma lógica anterior)
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> if (!granted) println("Permiso de cámara denegado") }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            localImageBitmap = it
            val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out -> it.compress(Bitmap.CompressFormat.JPEG, 100, out) }
            coroutineScope.launch { uploadedImageUrl = uploadImage(file) }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            val file = File(context.cacheDir, "gallery_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            coroutineScope.launch { uploadedImageUrl = uploadImage(file) }
        }
    }

    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeP)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil de Admin",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BrownP
        )

        Spacer(Modifier.height(24.dp))

        // --- SECCIÓN DE FOTO (Igual que antes) ---
        if (imageUri != null || localImageBitmap != null) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f).height(250.dp),
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

        // Botones de Foto
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { takePictureLauncher.launch(null) },
                colors = ButtonDefaults.buttonColors(containerColor = BrownP),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cámara", color = BeigeP)
            }
            Button(
                onClick = { selectImageLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = BrownP),
                modifier = Modifier.weight(1f)
            ) {
                Text("Galería", color = BeigeP)
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- NUEVA SECCIÓN: DATOS DEL ADMINISTRADOR ---
        Card(
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileItemAdmin(
                    icon = Icons.Default.Person,
                    label = "Nombre",
                    value = usuarioBackend?.nombre ?: "Cargando..."
                )
                Divider(color = BeigeP)

                ProfileItemAdmin(
                    icon = Icons.Default.Email,
                    label = "Correo",
                    value = usuarioBackend?.correo ?: "Cargando..."
                )
                Divider(color = BeigeP)

                // Teléfono (Dummy, igual que en User) - Dirección ELIMINADA
                ProfileItemAdmin(
                    icon = Icons.Default.Phone,
                    label = "Teléfono",
                    value = "+56 9 1234 5678 (Admin)"
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Botón Cerrar Sesión
        Button(
            onClick = {
                try {
                    authViewModel.logout()
                    navControllerApp.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                } catch (err: Exception) {}
            },
            colors = ButtonDefaults.buttonColors(containerColor = PurpleGrey40),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión", color = BeigeP)
        }
    }
}

// Componente para las filas de datos (reutilizado localmente para el admin)
@Composable
fun ProfileItemAdmin(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = BrownP, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, color = Black, fontWeight = FontWeight.SemiBold)
        }
    }
}
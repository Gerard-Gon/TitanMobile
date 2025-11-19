package com.example.titancake.data.model


data class UploadData(
    val url: String
)

data class UploadResponse(
    val status: String,
    val data: UploadData
)
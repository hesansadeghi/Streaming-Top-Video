package com.example.streamingtopvideo.models

data class DownloadUiModel(
    val id: String,
    val state: Int,
    val percentDownloaded: Float,
    val bytesDownloaded: Long
)

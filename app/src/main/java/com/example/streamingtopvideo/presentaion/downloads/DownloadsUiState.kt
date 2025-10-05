package com.example.streamingtopvideo.presentaion.downloads

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity

data class DownloadsUiState(
    val downloadedMovies: MutableList<DownloadedMovieEntity> = mutableListOf()
)
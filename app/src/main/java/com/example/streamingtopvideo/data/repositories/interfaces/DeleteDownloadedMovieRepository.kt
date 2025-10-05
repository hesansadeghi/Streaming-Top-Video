package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity

interface DeleteDownloadedMovieRepository {

    suspend fun deleteDownloadedMovie(entity: DownloadedMovieEntity)
}
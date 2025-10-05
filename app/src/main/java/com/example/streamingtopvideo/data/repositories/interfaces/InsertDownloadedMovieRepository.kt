package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity

interface InsertDownloadedMovieRepository {

    suspend fun insertDownloadedMovie(entity: DownloadedMovieEntity)
}
package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import kotlinx.coroutines.flow.Flow

interface GetAllDownloadedMoviesRepository {

    suspend fun getAllDownloadedMovies(): MutableList<DownloadedMovieEntity>
}
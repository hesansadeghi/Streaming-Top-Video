package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity

interface GetAllDownloadedMoviesRepository {

    suspend fun getAllDownloadedMovies(): MutableList<DownloadedMovieEntity>
}
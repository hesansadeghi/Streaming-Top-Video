package com.example.streamingtopvideo.data.repositories.interfaces

interface ExistsDownloadedMovieRepository {

    suspend fun existDownloadedMovie(id: String) : Boolean
}
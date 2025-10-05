package com.example.streamingtopvideo.data.repositories.interfaces

interface ExistsFavoriteMovieRepository {

    suspend fun existFavoriteMovie(id: Int) : Boolean
}
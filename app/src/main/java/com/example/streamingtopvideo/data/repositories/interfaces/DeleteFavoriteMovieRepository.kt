package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity

interface DeleteFavoriteMovieRepository {

    suspend fun deleteFavoriteMovie(entity: FavoriteMovieEntity)
}
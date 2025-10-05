package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity

interface InsertFavoriteMovieRepository {

    suspend fun insertFavoriteMovie(entity: FavoriteMovieEntity)
}
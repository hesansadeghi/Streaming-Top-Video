package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity

interface GetAllFavoriteMoviesRepository {

    suspend fun getAllFavoriteMovies(): MutableList<FavoriteMovieEntity>
}
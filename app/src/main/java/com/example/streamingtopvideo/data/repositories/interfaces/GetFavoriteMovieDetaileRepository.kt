package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity

interface GetFavoriteMovieDetaileRepository {

    suspend fun getFavoriteMovieDetaile(id: Int) :FavoriteMovieEntity
}
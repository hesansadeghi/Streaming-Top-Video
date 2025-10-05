package com.example.streamingtopvideo.data.repositories

import com.example.streamingtopvideo.data.db.dao.FavoriteMoviesDao
import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity
import com.example.streamingtopvideo.data.repositories.interfaces.GetAllFavoriteMoviesRepository
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val dao: FavoriteMoviesDao) :
    GetAllFavoriteMoviesRepository {


    override suspend fun getAllFavoriteMovies(): MutableList<FavoriteMovieEntity> {

        return dao.getAllFavoriteMovies()
    }
}
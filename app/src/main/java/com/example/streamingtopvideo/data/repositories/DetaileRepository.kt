package com.example.streamingtopvideo.data.repositories

import com.example.streamingtopvideo.data.api.ApiService
import com.example.streamingtopvideo.data.db.dao.FavoriteMoviesDao
import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity
import com.example.streamingtopvideo.data.models.ResponseDetaile
import com.example.streamingtopvideo.data.repositories.interfaces.DeleteFavoriteMovieRepository
import com.example.streamingtopvideo.data.repositories.interfaces.DetaileMovieByIdRepository
import com.example.streamingtopvideo.data.repositories.interfaces.ExistsFavoriteMovieRepository
import com.example.streamingtopvideo.data.repositories.interfaces.GetFavoriteMovieDetaileRepository
import com.example.streamingtopvideo.data.repositories.interfaces.InsertFavoriteMovieRepository
import com.example.streamingtopvideo.util.Resource
import com.example.streamingtopvideo.util.safeApiCall
import javax.inject.Inject

class DetaileRepository @Inject constructor(
    private val api: ApiService,
    private val dao: FavoriteMoviesDao
) : DetaileMovieByIdRepository, ExistsFavoriteMovieRepository,
    InsertFavoriteMovieRepository, DeleteFavoriteMovieRepository,
    GetFavoriteMovieDetaileRepository {

    override suspend fun detaileMovie(movieId: Int): Resource<ResponseDetaile> {
        return safeApiCall { api.detaileMovie(movieId = movieId) }
    }

    override suspend fun existFavoriteMovie(id: Int): Boolean {
        return dao.existsFavoriteMovie(id)
    }

    override suspend fun insertFavoriteMovie(entity: FavoriteMovieEntity) {
        dao.insertFavoriteMovie(entity)
    }

    override suspend fun deleteFavoriteMovie(entity: FavoriteMovieEntity) {
        dao.deleteFavoriteMovie(entity)
    }

    override suspend fun getFavoriteMovieDetaile(id: Int): FavoriteMovieEntity {
       return dao.getFavoriteMovie(id)
    }
}
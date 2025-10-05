package com.example.streamingtopvideo.data.repositories

import com.example.streamingtopvideo.data.api.ApiService
import com.example.streamingtopvideo.data.db.dao.DownloadedMoviesDao
import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.data.models.ResponseGenresList
import com.example.streamingtopvideo.data.models.ResponseMoviesList
import com.example.streamingtopvideo.data.repositories.interfaces.ExistsDownloadedMovieRepository
import com.example.streamingtopvideo.data.repositories.interfaces.GenresRepository
import com.example.streamingtopvideo.data.repositories.interfaces.InsertDownloadedMovieRepository
import com.example.streamingtopvideo.data.repositories.interfaces.LastMoviesRepository
import com.example.streamingtopvideo.data.repositories.interfaces.MoviesWithGenreRepository
import com.example.streamingtopvideo.util.Resource
import com.example.streamingtopvideo.util.safeApiCall
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: ApiService,
    private val dao: DownloadedMoviesDao
) : LastMoviesRepository, GenresRepository, MoviesWithGenreRepository,
    ExistsDownloadedMovieRepository, InsertDownloadedMovieRepository {

    override suspend fun lastMovies(): Resource<ResponseMoviesList> {

        return safeApiCall { api.lastMovies() }
    }


    override suspend fun getGenres(): Resource<ResponseGenresList> {

        return safeApiCall { api.getGenres() }
    }


    override suspend fun getMoviesWithGenre(id: Int): Resource<ResponseMoviesList> {

        return safeApiCall { api.getMoviesWithGenre(id) }
    }

    override suspend fun existDownloadedMovie(id: String): Boolean {

        return dao.existsDownloadedMovie(id)
    }

    override suspend fun insertDownloadedMovie(entity: DownloadedMovieEntity) {

        dao.insertDownloadedMovie(entity)
    }

}


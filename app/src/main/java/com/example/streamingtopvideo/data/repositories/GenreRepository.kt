package com.example.streamingtopvideo.data.repositories

import com.example.streamingtopvideo.data.api.ApiService
import com.example.streamingtopvideo.data.models.ResponseMoviesList
import com.example.streamingtopvideo.data.repositories.interfaces.MoviesWithGenreRepository
import com.example.streamingtopvideo.util.Resource
import com.example.streamingtopvideo.util.safeApiCall
import javax.inject.Inject

class GenreRepository @Inject constructor(
    private val api: ApiService
) : MoviesWithGenreRepository {

    override suspend fun getMoviesWithGenre(id: Int): Resource<ResponseMoviesList> {

        return safeApiCall { api.getMoviesWithGenre(id) }
    }
}
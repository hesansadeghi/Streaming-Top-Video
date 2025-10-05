package com.example.streamingtopvideo.data.repositories

import com.example.streamingtopvideo.data.api.ApiService
import com.example.streamingtopvideo.data.models.ResponseMoviesList
import com.example.streamingtopvideo.data.repositories.interfaces.GetsSearchMoviesByQueryRepository
import com.example.streamingtopvideo.util.Resource
import com.example.streamingtopvideo.util.safeApiCall
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: ApiService
) : GetsSearchMoviesByQueryRepository {

    override suspend fun getsSearchMoviesByQueryRepository(q: String): Resource<ResponseMoviesList> {
        return safeApiCall { api.searchMovies(q) }
    }
}
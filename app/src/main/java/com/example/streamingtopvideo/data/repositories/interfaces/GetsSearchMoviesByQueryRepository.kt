package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.models.ResponseMoviesList
import com.example.streamingtopvideo.util.Resource

interface GetsSearchMoviesByQueryRepository {

    suspend fun getsSearchMoviesByQueryRepository(q: String) :Resource<ResponseMoviesList>
}
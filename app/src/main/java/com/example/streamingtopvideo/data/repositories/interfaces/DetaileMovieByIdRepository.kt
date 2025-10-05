package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.models.ResponseDetaile
import com.example.streamingtopvideo.util.Resource

interface DetaileMovieByIdRepository {

    suspend fun detaileMovie(movieId: Int): Resource<ResponseDetaile>
}
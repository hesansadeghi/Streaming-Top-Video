package com.example.streamingtopvideo.data.repositories.interfaces

import com.example.streamingtopvideo.data.models.ResponseGenresList
import com.example.streamingtopvideo.util.Resource

interface GenresRepository {

    suspend fun getGenres() : Resource<ResponseGenresList>
}
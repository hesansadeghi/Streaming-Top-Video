package com.example.streamingtopvideo.data.uses_cases.detaile

import com.example.streamingtopvideo.data.repositories.DetaileRepository
import jakarta.inject.Inject

class GetExistFavoriteMovie @Inject constructor(private val repository: DetaileRepository) {

    suspend operator fun invoke(movieId: Int) = repository.existFavoriteMovie(movieId)
}
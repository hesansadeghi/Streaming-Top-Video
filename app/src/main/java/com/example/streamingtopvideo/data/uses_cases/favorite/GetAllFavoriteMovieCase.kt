package com.example.streamingtopvideo.data.uses_cases.favorite

import com.example.streamingtopvideo.data.repositories.FavoriteRepository
import jakarta.inject.Inject

class GetAllFavoriteMovieCase @Inject constructor(private val repository: FavoriteRepository) {

    suspend operator fun invoke() = repository.getAllFavoriteMovies()
}
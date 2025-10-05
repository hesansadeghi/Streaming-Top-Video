package com.example.streamingtopvideo.data.uses_cases.home

import com.example.streamingtopvideo.data.repositories.HomeRepository
import jakarta.inject.Inject

class GetExistsDownloadedMovieCase @Inject constructor(private val repository: HomeRepository) {

    suspend operator fun invoke(movieId: String) = repository.existDownloadedMovie(movieId)
}
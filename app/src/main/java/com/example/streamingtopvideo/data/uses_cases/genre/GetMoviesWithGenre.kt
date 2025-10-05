package com.example.streamingtopvideo.data.uses_cases.genre

import com.example.streamingtopvideo.data.repositories.GenreRepository
import jakarta.inject.Inject

class GetMoviesWithGenre @Inject constructor(private val repository: GenreRepository) {

    suspend operator fun invoke(id: Int) = repository.getMoviesWithGenre(id)
}
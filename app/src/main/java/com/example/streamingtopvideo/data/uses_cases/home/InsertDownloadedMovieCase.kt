package com.example.streamingtopvideo.data.uses_cases.home

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.data.repositories.HomeRepository
import jakarta.inject.Inject

class InsertDownloadedMovieCase @Inject constructor(private val repository: HomeRepository) {

    suspend operator fun invoke(entity: DownloadedMovieEntity) = repository.insertDownloadedMovie(entity)
}
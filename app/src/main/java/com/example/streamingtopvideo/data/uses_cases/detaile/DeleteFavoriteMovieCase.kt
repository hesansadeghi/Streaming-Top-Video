package com.example.streamingtopvideo.data.uses_cases.detaile

import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity
import com.example.streamingtopvideo.data.repositories.DetaileRepository
import jakarta.inject.Inject

class DeleteFavoriteMovieCase @Inject constructor(private val repository: DetaileRepository) {

    suspend operator fun invoke(entity: FavoriteMovieEntity) =
        repository.deleteFavoriteMovie(entity)
}
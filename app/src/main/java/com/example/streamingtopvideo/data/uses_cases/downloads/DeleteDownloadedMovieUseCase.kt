package com.example.streamingtopvideo.data.uses_cases.downloads

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.data.repositories.DownloadsRepository
import jakarta.inject.Inject

class DeleteDownloadedMovieUseCase @Inject constructor(private val repository: DownloadsRepository){

    suspend operator fun invoke(entity: DownloadedMovieEntity) = repository.deleteDownloadedMovie(entity)
}
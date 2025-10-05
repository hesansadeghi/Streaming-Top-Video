package com.example.streamingtopvideo.data.uses_cases.downloads

import com.example.streamingtopvideo.data.repositories.DownloadsRepository
import jakarta.inject.Inject

class GetAllDownloadedMoviesCase @Inject constructor(private val repository: DownloadsRepository) {

    suspend operator fun invoke() = repository.getAllDownloadedMovies()
}
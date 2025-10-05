package com.example.streamingtopvideo.data.uses_cases.downloads

data class DownloadsUseCase(
    val getAllDownloadedMoviesCase:GetAllDownloadedMoviesCase,
    val deleteDownloadedMovieUseCase:DeleteDownloadedMovieUseCase
)

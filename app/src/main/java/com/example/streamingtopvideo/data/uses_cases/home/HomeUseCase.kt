package com.example.streamingtopvideo.data.uses_cases.home

data class HomeUseCase(
    val lastMoviesCase: LastMoviesCase,
    val getGenresCase: GetGenresCase,
    val getMusicWithGenre: GetMusicWithGenre,
    val getFantasyMovieWithGenre:GetFantasyMovieWithGenre,
    val getExistsDownloadedMovieCase:GetExistsDownloadedMovieCase,
    val insertDownloadedMovieCase:InsertDownloadedMovieCase
)
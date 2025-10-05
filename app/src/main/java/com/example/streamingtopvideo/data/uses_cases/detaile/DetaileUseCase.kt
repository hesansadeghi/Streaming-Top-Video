package com.example.streamingtopvideo.data.uses_cases.detaile

data class DetaileUseCase(
    val getDetaileMovieCase: GetDetaileMovieCase,
    val getExistFavoriteMovie: GetExistFavoriteMovie,
    val insertFavoriteMovieCase: InsertFavoriteMovieCase,
    val deleteFavoriteMovieCase: DeleteFavoriteMovieCase,
    val getFavoriteMovieDetaileCase: GetFavoriteMovieDetaileCase
)

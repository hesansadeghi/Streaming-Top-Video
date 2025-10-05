package com.example.streamingtopvideo.presentaion.genre

import com.example.streamingtopvideo.data.models.ResponseMoviesList

data class GenreUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val moviesList: ResponseMoviesList? = null,
    val popBackStack: Boolean = false
)

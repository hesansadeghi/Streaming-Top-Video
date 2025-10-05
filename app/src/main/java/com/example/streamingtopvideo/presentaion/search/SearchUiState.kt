package com.example.streamingtopvideo.presentaion.search

import com.example.streamingtopvideo.data.models.ResponseMoviesList

data class SearchUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val moviesList: ResponseMoviesList? = null,
    val popBackStack: Boolean = false
)

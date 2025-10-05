package com.example.streamingtopvideo.presentaion.detaile

import com.example.streamingtopvideo.data.models.ResponseDetaile

data class DetaileUiState(
    val detaile:ResponseDetaile? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false,
    val popBackStack: Boolean = false
)
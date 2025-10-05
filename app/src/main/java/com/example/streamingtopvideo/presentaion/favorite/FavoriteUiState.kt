package com.example.streamingtopvideo.presentaion.favorite

import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity

data class FavoriteUiState(
    val favoriteMovieEntitiesList: MutableList<FavoriteMovieEntity> = mutableListOf()
)

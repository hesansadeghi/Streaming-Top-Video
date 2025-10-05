package com.example.streamingtopvideo.presentaion.favorite

import com.example.streamingtopvideo.presentaion.home.HomeUiEvent

sealed class FavoriteUiEvent {

    data object GetAllFavoriteMovies :FavoriteUiEvent()

    data class ClickedItemMovie(val movieId: Int?) : FavoriteUiEvent()

}
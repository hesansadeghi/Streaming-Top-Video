package com.example.streamingtopvideo.presentaion.favorite

sealed class FavoriteUiEvent {

    data object GetAllFavoriteMovies :FavoriteUiEvent()

    data class ClickedItemMovie(val movieId: Int?) : FavoriteUiEvent()

}
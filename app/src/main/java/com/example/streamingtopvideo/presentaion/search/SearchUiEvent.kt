package com.example.streamingtopvideo.presentaion.search

sealed class SearchUiEvent {

    data class SearchMoviesByQuery(val q: String):SearchUiEvent()

    data class ClickedItemMovie(val movieId: Int?) : SearchUiEvent()

    data object BackStack : SearchUiEvent()

}
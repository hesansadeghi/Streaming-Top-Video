package com.example.streamingtopvideo.presentaion.genre

sealed class GenreUiEvent {

    data class GetMoviesByGenre(val genreId: Int) : GenreUiEvent()

    data class ClickedItemMovie(val movieId: Int?) : GenreUiEvent()

    data object BackStack : GenreUiEvent()

}
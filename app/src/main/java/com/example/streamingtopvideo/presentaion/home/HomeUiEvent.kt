package com.example.streamingtopvideo.presentaion.home

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.data.models.ResponseGenresList

sealed class HomeUiEvent {

    data class PlayMovie(
        val url: String?,
        val id: String?,
        val titleMovie: String?
    ) : HomeUiEvent()

    data class DownloadMovie(val streamingMovie: StreamingMovie) : HomeUiEvent()
    data class CancelDownloadMovie(val downloadId: String) : HomeUiEvent()

    data class CheckDownloadMovieServiceIsRunning(val streamingMovie: StreamingMovie) : HomeUiEvent()

    data class ClickedItemGenre(val genre: ResponseGenresList.ResponseGenresListItem?) :
        HomeUiEvent()

    data class ClickedItemMovie(val movieId: Int?) : HomeUiEvent()
    data class CheckExistAndInsertDownloadedMovie(
        val movieId: String,
        val entity: DownloadedMovieEntity
    ) : HomeUiEvent()

    object GetAllData : HomeUiEvent()
    object LoadExistingDownloads : HomeUiEvent()

}
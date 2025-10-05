package com.example.streamingtopvideo.presentaion.downloads

import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity


sealed class DownloadsUiEvent{


    object GetAllData :DownloadsUiEvent()

    data class DeleteDownloadedMovie(val entity: DownloadedMovieEntity) : DownloadsUiEvent()


    data class PlayMovie(
        val url: String?,
        val id: String?,
        val titleMovie: String?
    ) : DownloadsUiEvent()
}
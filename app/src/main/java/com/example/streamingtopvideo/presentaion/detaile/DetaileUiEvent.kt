package com.example.streamingtopvideo.presentaion.detaile

import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity

sealed class DetaileUiEvent {

    data class GetDetaileMovieById(val movieId: Int) : DetaileUiEvent()
    data class InsertFavorite(val entity: FavoriteMovieEntity) : DetaileUiEvent()
    data class DeleteFavorite(val entity: FavoriteMovieEntity) : DetaileUiEvent()
    data object BackStack : DetaileUiEvent()

}
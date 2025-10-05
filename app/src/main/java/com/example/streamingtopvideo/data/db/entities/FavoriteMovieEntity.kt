package com.example.streamingtopvideo.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.streamingtopvideo.data.models.ResponseDetaile
import com.example.streamingtopvideo.util.Constants

@Entity(tableName = Constants.FAVORITE_MOVIE_TABLE)
data class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = false)
    var movieId: Int = 0,
    var responseDetaile: ResponseDetaile,
)

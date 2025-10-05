package com.example.streamingtopvideo.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.streamingtopvideo.util.Constants

@Entity(tableName = Constants.DOWNLOADED_MOVIE_TABLE)
data class DownloadedMovieEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val movieId: String = "",
    val coverImageResourceId: Int = -1,
    val title: String = "",
    val streamUrl: String = ""
)

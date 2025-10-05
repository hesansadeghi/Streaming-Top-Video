package com.example.streamingtopvideo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.streamingtopvideo.data.db.dao.DownloadedMoviesDao
import com.example.streamingtopvideo.data.db.dao.FavoriteMoviesDao
import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity


@Database(
    entities = [DownloadedMovieEntity::class, FavoriteMovieEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(TypeConvertorCustom::class)
abstract class MoviesDataBase : RoomDatabase() {

    abstract fun downloadedMoviesDao(): DownloadedMoviesDao

    abstract fun favoriteMoviesDao(): FavoriteMoviesDao

}
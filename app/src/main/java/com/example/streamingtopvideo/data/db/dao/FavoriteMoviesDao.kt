package com.example.streamingtopvideo.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity
import com.example.streamingtopvideo.util.Constants

@Dao
interface FavoriteMoviesDao {



    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertFavoriteMovie(entity: FavoriteMovieEntity)


    @Delete
    suspend fun deleteFavoriteMovie(entity: FavoriteMovieEntity)


    @Query("SELECT * FROM ${Constants.FAVORITE_MOVIE_TABLE} WHERE movieId=:movieId")
    fun getFavoriteMovie(movieId: Int): FavoriteMovieEntity


    @Query("SELECT * FROM ${Constants.FAVORITE_MOVIE_TABLE}")
    fun getAllFavoriteMovies(): MutableList<FavoriteMovieEntity>


    @Query("SELECT EXISTS (SELECT 1 FROM ${Constants.FAVORITE_MOVIE_TABLE} WHERE movieId = :movieId)")
    suspend fun existsFavoriteMovie(movieId: Int): Boolean


}
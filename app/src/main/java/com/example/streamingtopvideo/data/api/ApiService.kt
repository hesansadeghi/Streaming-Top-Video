package com.example.streamingtopvideo.data.api

import com.example.streamingtopvideo.data.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("movies")
    suspend fun lastMovies(): Response<ResponseMoviesList>


    @GET("genres/{genre_id}/movies")
    suspend fun getMoviesWithGenre(@Path("genre_id") id: Int): Response<ResponseMoviesList>


    @GET("genres")
    suspend fun getGenres(): Response<ResponseGenresList>


    @GET("movies")
    suspend fun searchMovies(@Query("q") q: String): Response<ResponseMoviesList>


    @GET("movies/{movie_id}")
    suspend fun detaileMovie(@Path("movie_id") movieId: Int): Response<ResponseDetaile>


}
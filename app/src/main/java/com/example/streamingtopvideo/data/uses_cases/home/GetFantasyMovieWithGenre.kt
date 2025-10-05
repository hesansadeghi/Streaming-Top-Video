package com.example.streamingtopvideo.data.uses_cases.home

import com.example.streamingtopvideo.data.repositories.HomeRepository
import com.example.streamingtopvideo.util.Constants.FANTASY_MOVIE_ID
import jakarta.inject.Inject

class GetFantasyMovieWithGenre @Inject constructor(private val repository: HomeRepository){

    suspend operator fun invoke()=repository.getMoviesWithGenre(FANTASY_MOVIE_ID)
}
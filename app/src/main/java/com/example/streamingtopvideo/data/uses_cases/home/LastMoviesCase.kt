package com.example.streamingtopvideo.data.uses_cases.home

import com.example.streamingtopvideo.data.repositories.HomeRepository
import javax.inject.Inject

class LastMoviesCase @Inject constructor(private val repository: HomeRepository){

    suspend operator fun invoke() = repository.lastMovies()

}
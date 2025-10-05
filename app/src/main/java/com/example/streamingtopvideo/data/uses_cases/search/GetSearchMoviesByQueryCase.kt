package com.example.streamingtopvideo.data.uses_cases.search

import com.example.streamingtopvideo.data.repositories.SearchRepository
import javax.inject.Inject

class GetSearchMoviesByQueryCase @Inject constructor(private val repository: SearchRepository) {

    suspend operator fun invoke(q: String) = repository.getsSearchMoviesByQueryRepository(q)

}
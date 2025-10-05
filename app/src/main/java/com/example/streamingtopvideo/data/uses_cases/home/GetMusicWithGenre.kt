package com.example.streamingtopvideo.data.uses_cases.home

import com.example.streamingtopvideo.data.repositories.HomeRepository
import com.example.streamingtopvideo.util.Constants.MUSIC_ID
import jakarta.inject.Inject

class GetMusicWithGenre @Inject constructor(private val repository: HomeRepository){

    suspend operator fun invoke()=repository.getMoviesWithGenre(MUSIC_ID)
}
package com.example.streamingtopvideo.presentaion.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamingtopvideo.data.uses_cases.genre.GenreUseCase
import com.example.streamingtopvideo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(private val genreUseCase: GenreUseCase) : ViewModel() {


    private val _uiState = MutableStateFlow(GenreUiState())
    val uiState = _uiState.asStateFlow()


    private val _detaileMovieNavigate = MutableStateFlow<Int?>(null)
    val detaileMovieNavigate: StateFlow<Int?> = _detaileMovieNavigate


    fun onEvent(event: GenreUiEvent) {

        when (event) {
            is GenreUiEvent.GetMoviesByGenre -> loadMoviesByGenre(event.genreId)
            is GenreUiEvent.ClickedItemMovie -> detaileMovie(event.movieId)
            is GenreUiEvent.BackStack -> popBackStack()
        }

    }


    private fun loadMoviesByGenre(genreId: Int) = viewModelScope.launch {

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        try {

            val moviesDeferred = async { genreUseCase.getMoviesWithGenre(genreId) }
            val moviesResult = moviesDeferred.await()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                moviesList = if (moviesResult is Resource.Success) moviesResult.data else null,
                errorMessage = if (moviesResult is Resource.Error) moviesResult.message else null
            )

        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = e.message ?: "Unknown error"
            )
        }



    }

    private fun detaileMovie(movieId: Int?) = viewModelScope.launch {

        _detaileMovieNavigate.value = movieId
    }


    private fun popBackStack() {

        _uiState.value = _uiState.value.copy(
            popBackStack = true
        )
    }

}
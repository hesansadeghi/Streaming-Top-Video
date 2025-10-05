package com.example.streamingtopvideo.presentaion.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamingtopvideo.data.uses_cases.favorite.FavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    private val _detaileMovieNavigate = MutableStateFlow<Int?>(null)
    val detaileMovieNavigate: StateFlow<Int?> = _detaileMovieNavigate


    fun onEvent(event: FavoriteUiEvent) {

        when (event) {
            is FavoriteUiEvent.GetAllFavoriteMovies -> loadAllFavoriteMovies()
            is FavoriteUiEvent.ClickedItemMovie -> detaileMovie(event.movieId)
        }

    }

    private fun loadAllFavoriteMovies() = viewModelScope.launch {

        val getAllFavoriteMovieDeferred = async { favoriteUseCase.getAllFavoriteMovieCase() }
        val getAllFavoriteMovieResult = getAllFavoriteMovieDeferred.await()

        _uiState.value = _uiState.value.copy(
            favoriteMovieEntitiesList = getAllFavoriteMovieResult
        )
    }


    private fun detaileMovie(movieId: Int?) = viewModelScope.launch {

        _detaileMovieNavigate.value = movieId
    }

}

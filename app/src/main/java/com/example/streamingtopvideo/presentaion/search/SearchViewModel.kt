package com.example.streamingtopvideo.presentaion.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamingtopvideo.data.uses_cases.search.SearchUseCase
import com.example.streamingtopvideo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()


    private val _detaileMovieNavigate = MutableStateFlow<Int?>(null)
    val detaileMovieNavigate: StateFlow<Int?> = _detaileMovieNavigate


    fun onEvent(event: SearchUiEvent) {

        when (event) {
            is SearchUiEvent.SearchMoviesByQuery -> searchMoviesByQuery(event.q)
            is SearchUiEvent.BackStack -> popBackStack()
            is SearchUiEvent.ClickedItemMovie -> detaileMovie(event.movieId)
        }

    }

    private fun searchMoviesByQuery(q: String) = viewModelScope.launch {

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        try {

            val searchedMoviesDeferred = async { searchUseCase.getSearchMoviesByQueryCase(q) }
            val searchedMoviesResult = searchedMoviesDeferred.await()


            _uiState.value = _uiState.value.copy(
                isLoading = false,
                moviesList = if (searchedMoviesResult is Resource.Success) searchedMoviesResult.data else null,
                errorMessage = if (searchedMoviesResult is Resource.Error) searchedMoviesResult.message else null
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
package com.example.streamingtopvideo.presentaion.detaile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity
import com.example.streamingtopvideo.data.uses_cases.detaile.DetaileUseCase
import com.example.streamingtopvideo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetaileViewModel @Inject constructor(
    private val detaileUseCase: DetaileUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(DetaileUiState())
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: DetaileUiEvent) {

        when (event) {
            is DetaileUiEvent.GetDetaileMovieById -> loadDetaileMovie(event.movieId)
            is DetaileUiEvent.BackStack -> popBackStack()
            is DetaileUiEvent.InsertFavorite -> insertFavorite(event.entity)
            is DetaileUiEvent.DeleteFavorite -> deleteFavorite(event.entity)
        }
    }

    private fun insertFavorite(entity: FavoriteMovieEntity) = viewModelScope.launch {


        val insertFavoriteMovieDeferred = async { detaileUseCase.insertFavoriteMovieCase(entity) }
        insertFavoriteMovieDeferred.await()

        _uiState.value = _uiState.value.copy(
            isFavorite = true,
        )
    }

    private fun deleteFavorite(entity: FavoriteMovieEntity) = viewModelScope.launch {

        val deleteFavoriteMovieDeferred = async { detaileUseCase.deleteFavoriteMovieCase(entity) }
        deleteFavoriteMovieDeferred.await()

        _uiState.value = _uiState.value.copy(
            isFavorite = false,
        )
    }

    private fun popBackStack() {

        _uiState.value = _uiState.value.copy(
            popBackStack = true
        )
    }

    private fun loadDetaileMovie(movieId: Int) = viewModelScope.launch {

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        val isFavoriteDeferred = async { detaileUseCase.getExistFavoriteMovie(movieId) }
        val isFavoriteResult = isFavoriteDeferred.await()


        if (isFavoriteResult){

            val favoriteMovieDetaileDeferred = async { detaileUseCase.getFavoriteMovieDetaileCase(movieId) }
            val favoriteMovieDetaileResult = favoriteMovieDetaileDeferred.await()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                detaile = favoriteMovieDetaileResult.responseDetaile,
                isFavorite = true,
            )


        }else{

            try {

                val detaileMovieDeferred = async { detaileUseCase.getDetaileMovieCase(movieId) }
                val detaileMovieResult = detaileMovieDeferred.await()


                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    detaile = if (detaileMovieResult is Resource.Success) detaileMovieResult.data else null,
                    isFavorite = false,
                    errorMessage = if (detaileMovieResult is Resource.Error) detaileMovieResult.message else null
                )


            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error"
                )
            }
        }


    }


}
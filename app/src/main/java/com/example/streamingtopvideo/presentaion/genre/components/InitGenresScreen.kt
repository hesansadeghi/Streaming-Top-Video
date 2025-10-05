package com.example.streamingtopvideo.presentaion.genre.components

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.presentaion.genre.GenreUiEvent
import com.example.streamingtopvideo.presentaion.genre.GenreViewModel
import com.example.streamingtopvideo.util.Constants.DETAILE
import com.example.streamingtopvideo.util.Constants.MOVIE_ID
import com.example.streamingtopvideo.util.Utils.popBackstack


@Composable
fun InitGenresScreen(
    title: String,
    navController: NavHostController,
    viewModel: GenreViewModel,
    errorMessage: (String) -> Unit,
    isAppBarVisible: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val detaileMovieNavigate: Int? = viewModel.detaileMovieNavigate.collectAsState().value

    val activity = LocalActivity.current


    if (uiState.popBackStack) {

        popBackstack(activity!!, navController)
    }


    LaunchedEffect(detaileMovieNavigate) {
        detaileMovieNavigate?.let { movieId ->

            try {
                navController.navigate("$DETAILE?$MOVIE_ID=$movieId")
                viewModel.onEvent(GenreUiEvent.ClickedItemMovie(null))
            } catch (e: Exception) {
                Log.e("NavigationError", "Error navigating to playMovie: ${e.message}")
            }
        }
    }


    Column {

        GenreTopAppBar(title) {

            viewModel.onEvent(GenreUiEvent.BackStack)
            isAppBarVisible()
        }


        if (uiState.isLoading) {

            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){

                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = Color.White
                )
            }

        } else {

            uiState.moviesList?.let {

                MoviesLazyVerticalStaggeredGrid(it) { movieId ->


                    viewModel.onEvent(GenreUiEvent.ClickedItemMovie(movieId))
                }
            }

            uiState.errorMessage?.let {

                errorMessage(it)
            }

        }

    }


}
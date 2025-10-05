package com.example.streamingtopvideo.presentaion.favorite

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.presentaion.favorite.components.LazyVerticalStaggeredGridFavoriteMovies
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine
import com.example.streamingtopvideo.util.Constants.DETAILE
import com.example.streamingtopvideo.util.Constants.MOVIE_ID


@Composable
fun FavoriteScreen(
    navController: NavHostController,
    viewModel: FavoriteViewModel = hiltViewModel(),
    isAppBarVisible: (Boolean) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val detaileMovieNavigate: Int? = viewModel.detaileMovieNavigate.collectAsState().value


    viewModel.onEvent(FavoriteUiEvent.GetAllFavoriteMovies)


    LaunchedEffect(detaileMovieNavigate) {
        detaileMovieNavigate?.let { movieId ->

            try {
                navController.navigate("$DETAILE?$MOVIE_ID=$movieId")
                viewModel.onEvent(FavoriteUiEvent.ClickedItemMovie(null))
            } catch (e: Exception) {
                Log.e("NavigationError", "Error navigating to playMovie: ${e.message}")
            }
        }
    }


    if (uiState.favoriteMovieEntitiesList.isNotEmpty()) {

        LazyVerticalStaggeredGridFavoriteMovies(
            uiState.favoriteMovieEntitiesList,
            { isAppBarVisibility ->

                isAppBarVisible(isAppBarVisibility)
            },
            { movieId ->

                viewModel.onEvent(FavoriteUiEvent.ClickedItemMovie(movieId))
                isAppBarVisible(false)
            })


    } else {

        Box(contentAlignment = Alignment.Center) {

            Icon(
                painter = painterResource(R.drawable.outline_file_download_off_150),
                contentDescription = "Favorite List is Empty",
                tint = DarkTangerine
            )
        }

    }

}
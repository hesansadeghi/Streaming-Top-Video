package com.example.streamingtopvideo.presentaion.genre

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.presentaion.genre.components.InitGenresScreen


@Composable
fun GenreScreen(
    navController: NavHostController,
    genreId: Int,
    genreTitle: String,
    viewModel: GenreViewModel = hiltViewModel(),
    isAppBarVisible: () -> Unit,
    errorMessage: (String) -> Unit
) {

    BackHandler {

        isAppBarVisible()
        viewModel.onEvent(GenreUiEvent.BackStack)
    }

    viewModel.onEvent(GenreUiEvent.GetMoviesByGenre(genreId))


    InitGenresScreen(
        genreTitle,
        navController,
        viewModel,
        {

        errorMessage(it)
    }, {

        isAppBarVisible()
    })


}
package com.example.streamingtopvideo.presentaion.detaile

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.presentaion.detaile.components.InitDetaileScreen


@Composable
fun DetaileScreen(
    navController: NavHostController,
    movieId: String,
    viewModel: DetaileViewModel = hiltViewModel(),
    isAppBarVisible: () -> Unit,
    errorMessage: (String) -> Unit
) {


    BackHandler {

        isAppBarVisible()
        viewModel.onEvent(DetaileUiEvent.BackStack)
    }

    viewModel.onEvent(DetaileUiEvent.GetDetaileMovieById(movieId.toInt()))

    InitDetaileScreen(
        navController,
        viewModel,
        { errorMessage ->

            errorMessage(errorMessage)
        },
        {

            isAppBarVisible()
            viewModel.onEvent(DetaileUiEvent.BackStack)
        })


}
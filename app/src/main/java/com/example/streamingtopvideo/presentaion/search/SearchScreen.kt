package com.example.streamingtopvideo.presentaion.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.presentaion.search.components.InitSearchScreen
import com.example.streamingtopvideo.presentaion.search.components.SearchTopAppBar


@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel(),
    isAppBarVisible: () -> Unit,
    errorMessage: (String) -> Unit
) {


    BackHandler {

        isAppBarVisible()
        viewModel.onEvent(SearchUiEvent.BackStack)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        SearchTopAppBar { text ->

            viewModel.onEvent(SearchUiEvent.SearchMoviesByQuery(text))
        }

        InitSearchScreen(navController, viewModel) { message ->

            errorMessage(message)
        }

    }


}
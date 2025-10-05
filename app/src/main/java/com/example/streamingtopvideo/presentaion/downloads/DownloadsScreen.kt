package com.example.streamingtopvideo.presentaion.downloads

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.models.PlayMovieModel
import com.example.streamingtopvideo.presentaion.downloads.components.DownloadedMoviesCard
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine
import com.example.streamingtopvideo.util.Constants.MOVIE_ID
import com.example.streamingtopvideo.util.Constants.MOVIE_TITLE
import com.example.streamingtopvideo.util.Constants.PLAY_MOVIE
import com.example.streamingtopvideo.util.Constants.STREAM_URL
import com.example.streamingtopvideo.util.ImmersiveSystemUi
import com.example.streamingtopvideo.util.ThisApp
import com.example.streamingtopvideo.util.setScreenOrientation
import kotlinx.coroutines.flow.observeOn


@Composable
fun DownloadsScreen(
    navController: NavHostController,
    viewModel: DownloadsViewModel = hiltViewModel(),
    isAppBarVisible: (Boolean) -> Unit
) {

    val activity = LocalActivity.current

    val uiState by viewModel.uiState.collectAsState()

    val playMovie: PlayMovieModel? = viewModel.playMovie.collectAsState().value

    setScreenOrientation(activity!!, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    ImmersiveSystemUi(false)


    LaunchedEffect(playMovie) {
        playMovie?.let { playMovie ->


            try {
                navController.navigate("$PLAY_MOVIE?$MOVIE_ID=${playMovie.id}&$STREAM_URL=${playMovie.streamUrl}&$MOVIE_TITLE=${playMovie.movieTitle}")
                viewModel.onEvent(DownloadsUiEvent.PlayMovie(null, null,null))
            } catch (e: Exception) {
                Log.e("NavigationError", "Error navigating to playMovie: ${e.message}")
            }
        }
    }


    if (uiState.downloadedMovies.isNotEmpty()){

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {

            items(uiState.downloadedMovies){ item->

                DownloadedMoviesCard(
                    item ,
                    { movieEntity ->

                        viewModel.onEvent(
                            DownloadsUiEvent.PlayMovie(
                                movieEntity.streamUrl,
                                movieEntity.movieId,
                                movieEntity.title
                            )
                        )
                        isAppBarVisible(false)

                        ThisApp.isLocaleMovie = true

                    },{ movieEntity->


                        viewModel.onEvent(DownloadsUiEvent.DeleteDownloadedMovie(movieEntity))
                    }
                )

            }


        }


    }else{

        Box(contentAlignment = Alignment.Center){

            Icon(
                painter = painterResource(R.drawable.outline_file_download_off_150),
                contentDescription = "Downloads List is Empty",
                tint = DarkTangerine )
        }
    }


}
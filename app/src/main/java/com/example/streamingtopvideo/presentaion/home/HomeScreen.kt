@file:kotlin.OptIn(ExperimentalPermissionsApi::class)

package com.example.streamingtopvideo.presentaion.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.data.models.ResponseGenresList.ResponseGenresListItem
import com.example.streamingtopvideo.models.PlayMovieModel
import com.example.streamingtopvideo.presentaion.home.components.CardStack
import com.example.streamingtopvideo.presentaion.home.components.GenresLazyRow
import com.example.streamingtopvideo.presentaion.home.components.HomeScreenShimmer
import com.example.streamingtopvideo.presentaion.home.components.MoviesLazyRow
import com.example.streamingtopvideo.presentaion.ui.theme.MidnightDark
import com.example.streamingtopvideo.util.Constants.DETAILE
import com.example.streamingtopvideo.util.Constants.FANTASY
import com.example.streamingtopvideo.util.Constants.GENRE
import com.example.streamingtopvideo.util.Constants.GENRE_ID
import com.example.streamingtopvideo.util.Constants.GENRE_TITLE
import com.example.streamingtopvideo.util.Constants.LAST_MOVIES
import com.example.streamingtopvideo.util.Constants.MOVIE_ID
import com.example.streamingtopvideo.util.Constants.MOVIE_TITLE
import com.example.streamingtopvideo.util.Constants.MUSIC
import com.example.streamingtopvideo.util.Constants.PLAY_MOVIE
import com.example.streamingtopvideo.util.Constants.STREAM_URL
import com.example.streamingtopvideo.util.ImmersiveSystemUi
import com.example.streamingtopvideo.util.ThisApp
import com.example.streamingtopvideo.util.setScreenOrientation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.map


@SuppressLint("InlinedApi")
@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    isAppBarVisible: (Boolean) -> Unit,
    errorMessage: (String) -> Unit
) {

    Log.i("screen", "HomeScreen")

    val uiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()

    var isSelectedStreamingMovie: StreamingMovie? = null

    val notificationPermission = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    ) {
        isSelectedStreamingMovie?.let { card ->

            viewModel.onEvent(HomeUiEvent.DownloadMovie(card))
            isSelectedStreamingMovie = null
        }
    }

    val activity = LocalActivity.current
    val context = LocalContext.current

    val playMovie: PlayMovieModel? = viewModel.playMovie.collectAsState().value
    val detaileMovieNavigate: Int? = viewModel.detaileMovieNavigate.collectAsState().value
    val moviesGenreNavigate: ResponseGenresListItem? =
        viewModel.moviesGenreNavigate.collectAsState().value


    setScreenOrientation(activity!!, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    ImmersiveSystemUi(false)


    LaunchedEffect(playMovie) {
        playMovie?.let { playMovie ->

            try {
                navController.navigate("$PLAY_MOVIE?$MOVIE_ID=${playMovie.id}&$STREAM_URL=${playMovie.streamUrl}&$MOVIE_TITLE=${playMovie.movieTitle}")
                viewModel.onEvent(HomeUiEvent.PlayMovie(null, null, null))
            } catch (e: Exception) {
                Log.e("NavigationError", "Error navigating to playMovie: ${e.message}")
            }
        }
    }

    LaunchedEffect(detaileMovieNavigate) {
        detaileMovieNavigate?.let { movieId ->

            try {
                navController.navigate("$DETAILE?$MOVIE_ID=$movieId")
                viewModel.onEvent(HomeUiEvent.ClickedItemMovie(null))
            } catch (e: Exception) {
                Log.e("NavigationError", "Error navigating to detaileMovie: ${e.message}")
            }
        }
    }


    LaunchedEffect(moviesGenreNavigate) {
        moviesGenreNavigate?.let { genre ->

            try {
                navController.navigate("$GENRE?$GENRE_ID=${genre.id}&$GENRE_TITLE=${genre.name}")
                viewModel.onEvent(HomeUiEvent.ClickedItemGenre(null))
            } catch (e: Exception) {
                Log.e("NavigationError", "Error navigating to genreMovie: ${e.message}")
            }
        }
    }

    if (uiState.isLoading) {


        HomeScreenShimmer()
    } else {

        // Detect scroll changes and update the visibility of the TopAppBar
        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .map { index -> index > 0 } // Check if we are scrolling down
                .collect { isScrollingDown ->
                    isAppBarVisible(!isScrollingDown) // تغییر وضعیت نمایش TopAppBar
                }
        }


        LazyColumn(
            modifier = Modifier.background(MidnightDark),
            state = listState
        ) {

            item {

                CardStack(
                    initialCards = uiState.streamingMovies,
                    viewModel = viewModel,
                    playItem = { streamMovie, isDownloaded ->

                        viewModel.onEvent(
                            HomeUiEvent.PlayMovie(
                                streamMovie.streamUrl,
                                streamMovie.id.toString(),
                                streamMovie.title
                            )
                        )
                        isAppBarVisible(false)

                        ThisApp.isLocaleMovie = isDownloaded
                    },
                    downLoadItem = { card ->

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                            if (!notificationPermission.status.isGranted) {

                                isSelectedStreamingMovie = card
                                notificationPermission.launchPermissionRequest()
                            } else {
                                viewModel.onEvent(HomeUiEvent.DownloadMovie(card))
                            }

                        } else {

                            viewModel.onEvent(HomeUiEvent.DownloadMovie(card))
                        }


                    },
                    { downloadId ->

                        viewModel.onEvent(HomeUiEvent.CancelDownloadMovie(downloadId))
                    })
            }


            item {

                uiState.genresList?.let {

                    GenresLazyRow(it) { genre ->

                        viewModel.onEvent(HomeUiEvent.ClickedItemGenre(genre))
                    }
                }
            }


            item {

                uiState.moviesList?.let {

                    MoviesLazyRow(LAST_MOVIES, it) { movieId ->

                        viewModel.onEvent(HomeUiEvent.ClickedItemMovie(movieId))
                    }
                }

            }


            item {

                uiState.fantasyMoviesList?.let {

                    MoviesLazyRow(FANTASY, it) { movieId ->

                        viewModel.onEvent(HomeUiEvent.ClickedItemMovie(movieId))
                    }
                }

            }


            item {

                uiState.musicList?.let {

                    MoviesLazyRow(MUSIC, it) { movieId ->

                        viewModel.onEvent(HomeUiEvent.ClickedItemMovie(movieId))
                    }
                }

            }


        }

        uiState.errorMessage?.let { message ->

            errorMessage(message)
        }

    }


}
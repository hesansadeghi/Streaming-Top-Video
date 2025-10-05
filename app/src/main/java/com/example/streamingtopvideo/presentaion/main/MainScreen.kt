package com.example.streamingtopvideo.presentaion.main


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.streamingtopvideo.models.NavigationItem
import com.example.streamingtopvideo.presentaion.detaile.DetaileScreen
import com.example.streamingtopvideo.presentaion.downloads.DownloadsScreen
import com.example.streamingtopvideo.presentaion.favorite.FavoriteScreen
import com.example.streamingtopvideo.presentaion.genre.GenreScreen
import com.example.streamingtopvideo.presentaion.home.HomeScreen
import com.example.streamingtopvideo.presentaion.main.components.NavBar
import com.example.streamingtopvideo.presentaion.main.components.TopAppView
import com.example.streamingtopvideo.presentaion.playback.PlayBackScreen
import com.example.streamingtopvideo.presentaion.search.SearchScreen
import com.example.streamingtopvideo.presentaion.ui.theme.MidnightDark
import com.example.streamingtopvideo.util.Constants.DETAILE
import com.example.streamingtopvideo.util.Constants.DOWNLOADS
import com.example.streamingtopvideo.util.Constants.FAVORITE
import com.example.streamingtopvideo.util.Constants.GENRE
import com.example.streamingtopvideo.util.Constants.GENRE_ID
import com.example.streamingtopvideo.util.Constants.GENRE_TITLE
import com.example.streamingtopvideo.util.Constants.HOME
import com.example.streamingtopvideo.util.Constants.MOVIE_ID
import com.example.streamingtopvideo.util.Constants.MOVIE_TITLE
import com.example.streamingtopvideo.util.Constants.PLAY_MOVIE
import com.example.streamingtopvideo.util.Constants.SEARCH
import com.example.streamingtopvideo.util.Constants.STREAM_URL
import com.example.streamingtopvideo.util.Utils.SetStatusBarColor
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {

    var selectedMenu by rememberSaveable {

        mutableStateOf(NavigationItem.Home.route)
    }

    val navController = rememberNavController()
    var fullScreen by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var isAppBarVisible by remember { mutableStateOf(true) }
    val appBarHeight = animateDpAsState(
        targetValue = if (isAppBarVisible) 75.dp else 0.dp,
        animationSpec = tween(durationMillis = 800)
    )

    SetStatusBarColor(false)

    Scaffold(
        modifier = Modifier.background(MidnightDark),
        topBar = {

            TopAppView(navController, appBarHeight.value)
        },
        bottomBar = {

            if (fullScreen.not()) {

                NavBar(navController, selectedMenu) { isSelectedMenuIndex ->
                    selectedMenu = isSelectedMenuIndex
                }
            }
        }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {

        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .background(MidnightDark)
                .padding(it),
            startDestination = HOME,
            navController = navController
        ) {

            composable(HOME) {

                fullScreen = false

                HomeScreen(
                    navController = navController,
                    errorMessage = { message ->

                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    isAppBarVisible = { isScrollingDown ->
                        isAppBarVisible = isScrollingDown
                    })

                if (selectedMenu != HOME) {
                    selectedMenu = HOME
                }

            }

            composable(FAVORITE) {

                FavoriteScreen(navController) { isScrollingDown ->
                    isAppBarVisible = isScrollingDown
                }

                fullScreen = false


                if (selectedMenu != FAVORITE) {
                    selectedMenu = FAVORITE
                }
            }

            composable(DOWNLOADS) {


                DownloadsScreen(navController) { isScrollingDown ->
                    isAppBarVisible = isScrollingDown
                }

                fullScreen = false


                if (selectedMenu != DOWNLOADS) {
                    selectedMenu = DOWNLOADS
                }

            }

            composable(SEARCH) {


                SearchScreen(
                    navController, errorMessage = { message ->

                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }

                    },
                    isAppBarVisible = {

                        isAppBarVisible = true
                    })

                navController.currentDestination?.route?.let { route ->

                    if (route == SEARCH) {

                        isAppBarVisible = false
                    }
                }

                fullScreen = true

            }

            val genreRoute = "$GENRE?$GENRE_ID={genreId}&$GENRE_TITLE={genreTitle}"

            composable(
                route = genreRoute,
                arguments = listOf(
                    navArgument(name = GENRE_ID) {
                        type = NavType.IntType
                    },
                    navArgument(name = GENRE_TITLE) {
                        type = NavType.StringType
                    }
                )) { backstackEntry ->


                backstackEntry.arguments?.getInt(GENRE_ID)?.let { genreId ->


                    backstackEntry.arguments?.getString(GENRE_TITLE)?.let { genreTitle ->

                        GenreScreen(
                            navController = navController,
                            genreId = genreId, genreTitle,
                            errorMessage = { errorMessage ->
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = errorMessage,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            isAppBarVisible = {

                                isAppBarVisible = true
                            })

                        navController.currentDestination?.route?.let { route ->

                            if (route == genreRoute) {

                                isAppBarVisible = false
                            }
                        }

                        fullScreen = true

                    }
                }
            }

            val playMovieRoute =
                "$PLAY_MOVIE?$MOVIE_ID={movieId}&$STREAM_URL={streamUrl}&$MOVIE_TITLE={movieTitle}"

            composable(
                route = playMovieRoute,
                arguments = listOf(
                    navArgument(name = MOVIE_ID) {
                        type = NavType.StringType
                    },
                    navArgument(name = STREAM_URL) {
                        type = NavType.StringType
                    },
                    navArgument(name = MOVIE_TITLE) {
                        type = NavType.StringType
                    }
                )
            ) { backstackEntry ->

                backstackEntry.arguments?.let { argument ->

                    argument.getString(STREAM_URL)?.let { streamUrl ->


                        argument.getString(MOVIE_TITLE)?.let { movieTitle ->


                            argument.getString(MOVIE_ID)?.let { movieId ->

                                PlayBackScreen(
                                    navController,
                                    streamUrl,
                                    movieId,
                                    movieTitle
                                ) {

                                    isAppBarVisible = true
                                }

                                fullScreen = true

                                navController.currentDestination?.route?.let { route ->

                                    if (route == playMovieRoute) {

                                        isAppBarVisible = false
                                    }
                                }

                            }
                        }

                    }


                }

            }

            val detaileRoute = "$DETAILE?$MOVIE_ID={movieId}"

            composable(
                route = detaileRoute,
                arguments = listOf(
                    navArgument(name = MOVIE_ID) {
                        type = NavType.StringType
                    }
                )
            ) { backstackEntry ->

                backstackEntry.arguments?.let { argument ->

                    argument.getString(MOVIE_ID)?.let { movieId ->

                        DetaileScreen(
                            navController = navController,
                            movieId = movieId,
                            errorMessage = { errorMessage ->

                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = errorMessage,
                                        duration = SnackbarDuration.Short
                                    )
                                }

                            }, isAppBarVisible = {

                                isAppBarVisible = true
                            })

                        navController.currentDestination?.route?.let { route ->

                            if (route == detaileRoute) {

                                isAppBarVisible = false
                            }
                        }

                        fullScreen = true

                    }

                }

            }


        }

    }

}

package com.example.streamingtopvideo.presentaion.playback

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.presentaion.playback.components.TrackSelector
import com.example.streamingtopvideo.presentaion.playback.components.VideoPlayer
import com.example.streamingtopvideo.presentaion.playback.components.VideoSpeedSelector
import com.example.streamingtopvideo.util.ImmersiveSystemUi
import com.example.streamingtopvideo.util.setScreenOrientation


@OptIn(UnstableApi::class)
@Composable
fun PlayBackScreen(
    navController: NavHostController,
    streamUrl: String,
    movieId: String,
    movieTitle: String,
    playerViewModel: PlayBackViewModel = hiltViewModel(),
    isAppBarVisible: () -> Unit
) {

    Log.i("screen", "PlayBackScreen")
    val activity = LocalActivity.current


    ImmersiveSystemUi(true)
    LaunchedEffect(true) {

        setScreenOrientation(activity!!, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        playerViewModel.onEvent(Init(streamUrl, movieId, movieTitle))
        playerViewModel.onEvent(Start())
    }


    BackHandler {

        playerViewModel.onEvent(BackStack)
        isAppBarVisible()
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        VideoPlayer(
            playerViewModel = playerViewModel,
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
        ){
            isAppBarVisible()
        }



        TrackSelector(
            playerViewModel = playerViewModel,
            onVideoTrackSelected = {
                playerViewModel.onEvent(SetVideoTrack(it))
            },
            onAudioTrackSelected = {
                playerViewModel.onEvent(SetAudioTrack(it))
            },
            onSubtitleTrackSelected = {
                playerViewModel.onEvent(SetSubtitleTrack(it))
            },
            onDismiss = {
                playerViewModel.hideTrackSelector()
            }
        )


        VideoSpeedSelector(
            playerViewModel = playerViewModel,
            onVideoSpeedSelected = {
                playerViewModel.onEvent(SetPlaybackParameters(it))
            },
            onDismiss = {
                playerViewModel.hideVideoSpeedSelector()
            }
        )

    }

}




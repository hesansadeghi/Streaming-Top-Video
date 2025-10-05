package com.example.streamingtopvideo.presentaion.playback.components

import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.presentaion.playback.AttachSurface
import com.example.streamingtopvideo.presentaion.playback.DetachSurface
import com.example.streamingtopvideo.presentaion.playback.PlayBackViewModel
import com.example.streamingtopvideo.util.ImmersiveSystemUi
import com.example.streamingtopvideo.util.Utils.popBackstack


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    playerViewModel: PlayBackViewModel,
    isAppBarVisible: () -> Unit
) {

    val playerUiModel by playerViewModel.playerUiModel.collectAsState()

    val activity = LocalActivity.current

    if (playerUiModel.popBackStack){

        ImmersiveSystemUi(false)
        popBackstack(activity!!,navController)
        isAppBarVisible()
    }

    Box {

        AndroidExternalSurface(
            modifier = modifier
                .aspectRatio(playerUiModel.videoAspectRatio)
                .clickable {

                    playerViewModel.showPlayerControls()
                }) {
            onSurface { surface, _, _ ->

                playerViewModel.onEvent(AttachSurface(surface))
                surface.onDestroyed {

                    playerViewModel.onEvent(DetachSurface)
                }
            }
        }


        VideoOverlay(
            modifier = Modifier.matchParentSize(),
            playerViewModel = playerViewModel,
            onHideControlsClick = {

                playerViewModel.hidePlayerControls()
            },
            onShowControlsClick = {

                playerViewModel.showPlayerControls()
            },
            onSettingClick = {

                playerViewModel.openTrackSelector()
            },
            onVideoSpeedClick={

                playerViewModel.openVideoSpeedSelector()
            },
            onAction = {

                playerViewModel.onEvent(playerUiEvent = it)
            },
        )

    }

}
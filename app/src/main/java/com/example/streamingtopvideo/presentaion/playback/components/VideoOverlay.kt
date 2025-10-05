package com.example.streamingtopvideo.presentaion.playback.components

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.example.streamingtopvideo.presentaion.playback.PlayBackViewModel
import com.example.streamingtopvideo.presentaion.playback.PlayerUiEvent


@OptIn(UnstableApi::class)
@Composable
fun VideoOverlay(
    modifier: Modifier = Modifier,
    playerViewModel: PlayBackViewModel,
    onHideControlsClick: () -> Unit,
    onShowControlsClick: () -> Unit,
    onSettingClick: () -> Unit,
    onVideoSpeedClick: () -> Unit,
    onAction: (PlayerUiEvent) -> Unit,
) {

    val playerUiModel by playerViewModel.playerUiModel.collectAsState()

    Box(
        modifier = modifier
    ) {


        if (playerUiModel.playerControlsVisible) {
            PlayBackControls(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = onHideControlsClick),
                playerUiModel = playerUiModel,
                onSettingClick = onSettingClick,
                onVideoSpeedClick = onVideoSpeedClick,
                onAction = onAction
            )
        }

        Subtitle(
            modifier = Modifier
                .matchParentSize()
                .clickable(onClick = onShowControlsClick),
            cues = playerUiModel.currentSubtitles
        )

    }
}
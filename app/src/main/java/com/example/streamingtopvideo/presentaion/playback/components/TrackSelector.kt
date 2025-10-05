package com.example.streamingtopvideo.presentaion.playback.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.streamingtopvideo.models.TrackState
import com.example.streamingtopvideo.presentaion.playback.AudioTrack
import com.example.streamingtopvideo.presentaion.playback.PlayBackViewModel
import com.example.streamingtopvideo.presentaion.playback.SubtitleTrack
import com.example.streamingtopvideo.presentaion.playback.VideoTrack
import com.example.streamingtopvideo.presentaion.ui.theme.EbonyClay


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackSelector(
    playerViewModel: PlayBackViewModel,
    onVideoTrackSelected: (VideoTrack) -> Unit,
    onAudioTrackSelected: (AudioTrack) -> Unit,
    onSubtitleTrackSelected: (SubtitleTrack) -> Unit,
    onDismiss: () -> Unit
) {

    val playerUiModel by playerViewModel.playerUiModel.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (playerUiModel.isTrackSelectorVisible) {

        playerUiModel.trackSelectionUiModel?.let { trackUiModel ->

            var currentState by remember { mutableStateOf(TrackState.LIST) }

            ModalBottomSheet(
                containerColor = EbonyClay,
                onDismissRequest = onDismiss,
                sheetState = sheetState
            ) {

                when (currentState) {
                    TrackState.LIST -> {
                        Column {
                            Text(
                                text = "Video Tracks",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        currentState = TrackState.VIDEO
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                color = Color.White
                            )
                            Text(
                                text = "Audio Tracks",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        currentState = TrackState.AUDIO
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                color = Color.White
                            )
                            Text(
                                text = "Subtitle Tracks",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        currentState = TrackState.SUBTITLE
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                color = Color.White
                            )
                        }
                    }

                    TrackState.VIDEO -> {

                        Column {
                            trackUiModel.videoTracks.forEach { videoTrack ->

                                Text(
                                    text = videoTrack.displayName,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            onVideoTrackSelected(videoTrack)
                                            onDismiss()
                                        }
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = if (videoTrack == trackUiModel.selectedVideoTrack) Color.Yellow else Color.White
                                )
                            }
                        }

                    }

                    TrackState.AUDIO -> {

                        Column {
                            trackUiModel.audioTracks.forEach { audioTrack ->

                                Text(
                                    text = audioTrack.displayName,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            onAudioTrackSelected(audioTrack)
                                            onDismiss()
                                        }
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = if (audioTrack == trackUiModel.selectedAudioTrack) Color.Yellow else Color.White
                                )
                            }
                        }

                    }

                    TrackState.SUBTITLE -> {

                        Column {
                            trackUiModel.subtitleTracks.forEach { subtitleTrack ->

                                Text(
                                    text = subtitleTrack.displayName,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            onSubtitleTrackSelected(subtitleTrack)
                                            onDismiss()
                                        }
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = if (subtitleTrack == trackUiModel.selectedSubtitleTrack) Color.Yellow else Color.White
                                )
                            }
                        }

                    }
                }

            }

        }
    }

}
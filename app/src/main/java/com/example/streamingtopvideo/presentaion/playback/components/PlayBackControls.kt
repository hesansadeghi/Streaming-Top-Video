package com.example.streamingtopvideo.presentaion.playback.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.presentaion.playback.BackStack
import com.example.streamingtopvideo.presentaion.playback.FastForward
import com.example.streamingtopvideo.presentaion.playback.Pause
import com.example.streamingtopvideo.presentaion.playback.PlaybackState.BUFFERING
import com.example.streamingtopvideo.presentaion.playback.PlaybackState.COMPLETED
import com.example.streamingtopvideo.presentaion.playback.PlaybackState.ERROR
import com.example.streamingtopvideo.presentaion.playback.PlaybackState.IDLE
import com.example.streamingtopvideo.presentaion.playback.PlaybackState.PAUSE
import com.example.streamingtopvideo.presentaion.playback.PlaybackState.PLAYING
import com.example.streamingtopvideo.presentaion.playback.PlayerUiEvent
import com.example.streamingtopvideo.presentaion.playback.PlayerUiModel
import com.example.streamingtopvideo.presentaion.playback.Resume
import com.example.streamingtopvideo.presentaion.playback.Rewind
import com.example.streamingtopvideo.presentaion.playback.Seek
import com.example.streamingtopvideo.presentaion.playback.Start
import com.example.streamingtopvideo.presentaion.playback.isReady
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine


@OptIn(UnstableApi::class)
@Composable
fun PlayBackControls(
    modifier: Modifier = Modifier,
    playerUiModel: PlayerUiModel,
    onSettingClick: () -> Unit,
    onVideoSpeedClick: () -> Unit,
    onAction: (PlayerUiEvent) -> Unit,
) {

    Box(
        modifier = modifier
            .background(Color.Transparent)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = playerUiModel.titleMovie,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )


            PlayBackButton(
                modifier = Modifier.size(45.dp).clip(CircleShape),
                resourceId = R.drawable.rounded_arrow_circle_right_24,
                description = "Back stack button",
                DarkTangerine
            ) {

                onAction(BackStack)
            }

        }


        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (playerUiModel.playbackState == BUFFERING) {

                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = Color.White
                )
            }


            if (playerUiModel.playbackState == COMPLETED) {

                PlayBackButton(
                    modifier = Modifier.size(45.dp),
                    resourceId = R.drawable.icons8_replay_50,
                    description = "Replay",
                    color = Color.White
                ) {

                    onAction(Start(0))
                }
            }


            if (playerUiModel.playbackState == ERROR) {

                PlayBackButton(
                    modifier = Modifier.size(45.dp),
                    resourceId = R.drawable.icons8_error_50,
                    description = "Error",
                    color = null
                ) {


                }

                PlayBackButton(
                    modifier = Modifier.size(45.dp),
                    resourceId = R.drawable.icons8_replay_50,
                    description = "Replay",
                    color = Color.White
                ) {

                    onAction(Start(playerUiModel.timelineUiModel?.currentPositionInMs))
                }
            }


        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (playerUiModel.playbackState == PLAYING) {

                PlayBackCircleButton(
                    modifier = Modifier.size(35.dp),
                    resourceId = R.drawable.icons8_pause_64,
                    description = "pause",
                    color = Color.White
                ) {

                    onAction(Pause)
                }
            }


            if (playerUiModel.playbackState == PAUSE) {

                PlayBackCircleButton(
                    modifier = Modifier.size(35.dp),
                    resourceId = R.drawable.icons8_play_64,
                    description = "play",
                    color = Color.White
                ) {

                    onAction(Resume)
                }

            }


            if (playerUiModel.playbackState == IDLE) {

                PlayBackCircleButton(
                    modifier = Modifier.size(35.dp),
                    resourceId = R.drawable.icons8_play_64,
                    description = "Start",
                    color = Color.White
                ) {

                    onAction(Start())
                }
            }


            if (playerUiModel.playbackState.isReady()) {

                PlayBackButton(
                    modifier = Modifier.size(32.dp),
                    resourceId = R.drawable.icons8_replay_10_96,
                    description = "Fast forward",
                    color = DarkTangerine
                ) {

                    onAction(Rewind(10_000))

                }


                PlayBackButton(
                    modifier = Modifier.size(32.dp),
                    resourceId = R.drawable.icons8_forward_10_96,
                    description = "Fast forward",
                    color = DarkTangerine
                ) {

                    onAction(FastForward(10_000))

                }

            }





            playerUiModel.timelineUiModel?.let { timeLine ->

                Row(
                    modifier = Modifier.weight(8f)
                ) {

                    PlayBackPosition(
                        contentDurationInMs = timeLine.durationInMs,
                        contentPositionInMs = timeLine.currentPositionInMs
                    )

                    TimeBar(
                        modifier = Modifier,
                        positionInMs = timeLine.currentPositionInMs,
                        durationInMs = timeLine.durationInMs,
                        bufferedPositionInMs = timeLine.bufferedPositionInMs
                    ) {

                        onAction(Seek(it.toLong()))
                    }

                }

                PlayBackButton(
                    modifier = Modifier.size(32.dp),
                    resourceId = R.drawable.baseline_speed_24,
                    description = "Open Video Speed Selector",
                    color = DarkTangerine
                ) {

                    onVideoSpeedClick()
                }


                PlayBackButton(
                    modifier = Modifier.size(32.dp),
                    resourceId = R.drawable.icons8_setting_50,
                    description = "Open Track Selector",
                    color = DarkTangerine
                ) {

                    onSettingClick()
                }

            }


        }


    }
}
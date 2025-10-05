package com.example.streamingtopvideo.presentaion.playback.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.streamingtopvideo.presentaion.playback.PlayBackViewModel
import com.example.streamingtopvideo.presentaion.ui.theme.EbonyClay


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoSpeedSelector(
    playerViewModel: PlayBackViewModel,
    onVideoSpeedSelected: (Float) -> Unit,
    onDismiss: () -> Unit
) {

    val playerUiModel by playerViewModel.playerUiModel.collectAsState()

    if (playerUiModel.isVideoSpeedVisible) {

        playerUiModel.videoSpeedUiModel?.let { speedUiModel ->


            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )

            ModalBottomSheet(
                containerColor = EbonyClay,
                onDismissRequest = onDismiss,
                sheetState = sheetState
            ) {

                Column {
                    speedUiModel.speedValues.forEach { speed ->

                        Text(
                            text = speed.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    onVideoSpeedSelected(speed)
                                    onDismiss()
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (speed == speedUiModel.selectedSpeed) Color.Yellow else Color.White
                        )
                    }
                }

            }

        }


    }


}
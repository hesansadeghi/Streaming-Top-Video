package com.example.streamingtopvideo.presentaion.playback.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.streamingtopvideo.util.Utils


@Composable
fun PlayBackPosition(
    contentPositionInMs: Long,
    contentDurationInMs: Long
) {

    val contentPositionString = Utils.formatMsToString(contentPositionInMs)
    val contentDurationString = Utils.formatMsToString(contentDurationInMs)

    Text(
        text = "$contentPositionString / $contentDurationString",
        fontSize = 14.sp,
        color = Color.White
    )

}
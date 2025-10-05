package com.example.streamingtopvideo.presentaion.playback.components

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.text.Cue
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.SubtitleView


@OptIn(UnstableApi::class)
@Composable
fun Subtitle(
    modifier: Modifier = Modifier,
    cues: List<Cue>
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->
            SubtitleView(context)

        },
        update = { subtitleView ->

            subtitleView.setCues(cues)
        }
    )

}
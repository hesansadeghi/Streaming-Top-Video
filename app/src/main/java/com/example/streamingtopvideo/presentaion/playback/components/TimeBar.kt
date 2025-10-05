package com.example.streamingtopvideo.presentaion.playback.components

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.TimeBar
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine
import com.example.streamingtopvideo.presentaion.ui.theme.PurpleGrey40


@OptIn(UnstableApi::class)
@Composable
fun TimeBar(
    modifier: Modifier,
    positionInMs: Long,
    durationInMs: Long,
    bufferedPositionInMs: Long,
    onSeek: (Float) -> Unit
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->

            DefaultTimeBar(context).apply {
                setScrubberColor(DarkTangerine.hashCode())
                setPlayedColor(DarkTangerine.hashCode())
                setBufferedColor(DarkTangerine.hashCode())
                setUnplayedColor(PurpleGrey40.hashCode())
            }

        },
        update = { timeBar ->

            with(timeBar) {

                addListener(object : TimeBar.OnScrubListener {
                    override fun onScrubStart(
                        timeBar: TimeBar,
                        position: Long
                    ) {
                    }

                    override fun onScrubMove(
                        timeBar: TimeBar,
                        position: Long
                    ) {
                    }

                    override fun onScrubStop(
                        timeBar: TimeBar,
                        position: Long,
                        canceled: Boolean
                    ) {
                        onSeek(position.toFloat())
                    }
                })

                setPosition(positionInMs)
                setDuration(durationInMs)
                setBufferedPosition(bufferedPositionInMs)

            }

        }
    )

}
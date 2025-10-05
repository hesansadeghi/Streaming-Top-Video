package com.example.streamingtopvideo.presentaion.home.components

import androidx.annotation.OptIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.presentaion.home.HomeUiEvent
import com.example.streamingtopvideo.presentaion.home.HomeViewModel
import com.example.streamingtopvideo.presentaion.home.StreamingMovie
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine
import com.example.streamingtopvideo.util.Utils.formatFileSize
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(UnstableApi::class)
@Composable
fun CardStack(
    initialCards: List<StreamingMovie>,
    viewModel: HomeViewModel,
    playItem: (StreamingMovie, Boolean) -> Unit,
    downLoadItem: (StreamingMovie) -> Unit,
    cancelDownLoadItem: (String) -> Unit,
) {

    val cards = remember { mutableStateListOf(*initialCards.toTypedArray()) }
    val scope = rememberCoroutineScope()

    val downloads by viewModel.downloads.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {

        cards.forEachIndexed { index, card ->

            val downloadUiModel = downloads[card.id.toString()]


            val offsetX = remember { Animatable(0f) }
            val rotation = derivedStateOf { offsetX.value / 20f }
            val scale = 1f - (index * 0.05f)
            val offsetY = -(index * 20).dp

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .offset(y = offsetY)
                    .graphicsLayer(
                        rotationZ = if (index == 0) rotation.value else 0f,
                        scaleX = scale,
                        scaleY = scale
                    )
                    .zIndex((cards.size - index).toFloat())
                    .background(Color.Gray, RoundedCornerShape(16.dp))
                    .size(250.dp, 250.dp)
                    .pointerInput(card) {
                        if (index == 0) {
                            detectDragGestures(
                                onDragEnd = {
                                    scope.launch {
                                        when {
                                            offsetX.value > 300f -> {
                                                offsetX.animateTo(1000f, tween(300))
                                                val removed = cards.removeAt(0)
                                                cards.add(removed)
                                                offsetX.snapTo(0f) // ریست کارت بعدی
                                            }

                                            offsetX.value < -300f -> {
                                                offsetX.animateTo(-1000f, tween(300))
                                                val removed = cards.removeAt(0)
                                                cards.add(removed)
                                                offsetX.snapTo(0f)
                                            }

                                            else -> {
                                                offsetX.animateTo(0f, spring())
                                            }
                                        }
                                    }
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                }
                            }
                        }
                    },
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    Image(
                        painter = painterResource(card.coverImageResourceId),
                        contentDescription = "Image Cover",
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop
                    )


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Black,
                                    )
                                )
                            )
                    )


                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier.weight(5f),
                            text = "${card.title} \n${card.movieSize}${
                                if (downloadUiModel?.state == Download.STATE_DOWNLOADING || downloadUiModel?.state == Download.STATE_QUEUED) {
                                    "\\${formatFileSize(downloadUiModel.bytesDownloaded)}"
                                } else ""
                            }",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        when (downloadUiModel?.state) {
                            null -> {
                                IconButton(onClick = {
                                    downLoadItem(card)
                                }) {
                                    Icon(
                                        modifier = Modifier.size(35.dp),
                                        painter = painterResource(R.drawable.outline_download_24),
                                        contentDescription = "Download Movie",
                                        tint = Color.White
                                    )
                                }
                            }

                            Download.STATE_DOWNLOADING, Download.STATE_QUEUED -> {

                                Box(contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(
                                        progress = {
                                            (downloadUiModel.percentDownloaded / 100f).coerceIn(
                                                0f,
                                                1f
                                            )
                                        },
                                        modifier = Modifier.size(35.dp),
                                        color = Color.White,
                                        strokeWidth = 3.dp,
                                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                                        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                                    )
                                    Text(
                                        text = "${downloadUiModel.percentDownloaded.toInt()}%",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }


                                viewModel.onEvent(
                                    HomeUiEvent.CheckDownloadMovieServiceIsRunning(card)
                                )
                            }

                            Download.STATE_COMPLETED -> {
                                Icon(
                                    painter = painterResource(R.drawable.outline_check_24),
                                    contentDescription = "Downloaded",
                                    tint = Color.Green,
                                    modifier = Modifier.size(35.dp)
                                )


                                val entity = DownloadedMovieEntity(
                                    movieId = card.id.toString(),
                                    coverImageResourceId = card.coverImageResourceId,
                                    title = card.title,
                                    streamUrl = card.streamUrl
                                )


                                viewModel.onEvent(
                                    HomeUiEvent.CheckExistAndInsertDownloadedMovie(
                                        card.id.toString(), entity
                                    )
                                )


                                Log.e("downloadId", "STATE_COMPLETED   ${card.id}")
                            }

                            else -> {

                                IconButton(onClick = {
                                    downLoadItem(card)
                                }) {
                                    Icon(
                                        modifier = Modifier.size(35.dp),
                                        painter = painterResource(R.drawable.outline_download_24),
                                        contentDescription = "Download Movie",
                                        tint = Color.White
                                    )
                                }
                            }
                        }


                        IconButton(onClick = {
                            playItem(
                                card,
                                (downloadUiModel?.state == Download.STATE_COMPLETED)
                            )
                        }) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                painter = painterResource(R.drawable.baseline_play_circle_outline_24),
                                contentDescription = "Play Movie",
                                tint = Color.White
                            )
                        }

                    }

                    if (downloadUiModel?.state == Download.STATE_DOWNLOADING || downloadUiModel?.state == Download.STATE_QUEUED) {

                        IconButton(
                            modifier = Modifier.align(Alignment.TopStart),
                            onClick = {
                            cancelDownLoadItem(downloadUiModel.id)
                        }) {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(R.drawable.outline_cancel_24),
                                contentDescription = "Cancel Download Movie",
                                tint = DarkTangerine
                            )
                        }

                    }

                }
            }
        }
    }
}




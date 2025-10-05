package com.example.streamingtopvideo.presentaion.downloads.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine


@Composable
fun DownloadedMoviesCard(
    entity: DownloadedMovieEntity,
    playItem: (DownloadedMovieEntity) -> Unit,
    deleteItem: (DownloadedMovieEntity) -> Unit
) {


    Card(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(8.dp)) {

        Box(modifier = Modifier.fillMaxSize()) {


            Image(
                painter = painterResource(entity.coverImageResourceId),
                contentDescription = "Image Cover",
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )


            Row(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .fillMaxWidth()
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.weight(5f).padding(start = 8.dp),
                    text = entity.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )


                IconButton(onClick = {
                    deleteItem(
                        entity
                    )
                }) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(R.drawable.baseline_delete_24),
                        contentDescription = "Delete Movie",
                        tint = DarkTangerine
                    )
                }


                IconButton(onClick = {
                    playItem(
                        entity
                    )
                }) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(R.drawable.baseline_play_circle_outline_24),
                        contentDescription = "Play Movie",
                        tint = DarkTangerine
                    )
                }

            }

        }


    }


}
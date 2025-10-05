package com.example.streamingtopvideo.presentaion.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamingtopvideo.data.models.ResponseGenresList
import com.example.streamingtopvideo.data.models.ResponseGenresList.ResponseGenresListItem
import com.example.streamingtopvideo.util.Constants.GENRES

@Composable
fun GenresLazyRow(items: ResponseGenresList, clickItem: (ResponseGenresListItem) -> Unit) {

    Column {

        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            text = GENRES,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        LazyRow {

            items(items) { item ->

                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(
                            Color.Gray, RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {

                            clickItem(item)
                        }
                       ,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray
                    )
                ) {


                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        text = item.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                }

            }


        }

    }

}

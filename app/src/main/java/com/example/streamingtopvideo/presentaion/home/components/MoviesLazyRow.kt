package com.example.streamingtopvideo.presentaion.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamingtopvideo.data.models.ResponseMoviesList
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun MoviesLazyRow(title: String, items: ResponseMoviesList, clickItem: (Int) -> Unit) {

    Column {

        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            text = title,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        LazyRow {

            items(items.data) { item ->


                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(145.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {

                            clickItem(item.id)
                        }
                ) {

                    Card(
                        modifier = Modifier
                            .height(220.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {


                        GlideImage(
                            modifier = Modifier.fillMaxWidth(),
                            // CoilImage, FrescoImage
                            imageModel = { item.poster },

                            // shows an error text if fail to load an image.
                            failure = {
                                Text(text = "image request failed.")
                            }
                        )

                    }

                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        text = item.title.toString(),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )


                }

            }


        }

    }

}
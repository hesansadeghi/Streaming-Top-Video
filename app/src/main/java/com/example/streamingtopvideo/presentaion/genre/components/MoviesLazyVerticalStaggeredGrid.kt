package com.example.streamingtopvideo.presentaion.genre.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamingtopvideo.data.models.ResponseMoviesList
import com.example.streamingtopvideo.presentaion.ui.theme.EbonyClay
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun MoviesLazyVerticalStaggeredGrid(items: ResponseMoviesList, clickItem: (Int) -> Unit) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp)
    ) {

        items(items.data) { item ->

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
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
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(EbonyClay),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = "image request failed.",
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        loading = {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(EbonyClay)
                            )

                        }
                    )

                }

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    text = item.title!!,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )


            }

        }

    }

}
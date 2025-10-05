package com.example.streamingtopvideo.presentaion.detaile.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity
import com.example.streamingtopvideo.presentaion.detaile.DetaileUiEvent
import com.example.streamingtopvideo.presentaion.detaile.DetaileViewModel
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine
import com.example.streamingtopvideo.presentaion.ui.theme.EbonyClay
import com.example.streamingtopvideo.presentaion.ui.theme.ImpactFont
import com.example.streamingtopvideo.presentaion.ui.theme.MidnightDark
import com.example.streamingtopvideo.util.Constants.ACTORS
import com.example.streamingtopvideo.util.Constants.IMAGES
import com.example.streamingtopvideo.util.Constants.IMDB
import com.example.streamingtopvideo.util.Utils.popBackstack
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun InitDetaileScreen(
    navController: NavHostController,
    viewModel: DetaileViewModel,
    errorMessage: (String) -> Unit,
    backStack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var detaileInfoLayoutColumnHeight by remember { mutableIntStateOf(0) }

    val activity = LocalActivity.current

    if (uiState.popBackStack) {

        popBackstack(activity!!, navController)
    }


    if (uiState.isLoading) {

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){

            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Color.White
            )
        }

    } else {


        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {


                uiState.detaile?.let { detaile ->


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {


                        GlideImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            // CoilImage, FrescoImage
                            imageModel = { detaile.poster },

                            // shows an error text if fail to load an image.
                            failure = {
                                Text(text = "image request failed.")
                            }
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
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

                        TopDetaileScreen(
                            uiState.isFavorite,
                            { isFavorite ->

                                val entity = FavoriteMovieEntity(
                                    detaile.id,
                                    detaile
                                )

                                if (isFavorite) {
                                    viewModel.onEvent(DetaileUiEvent.InsertFavorite(entity))
                                } else {
                                    viewModel.onEvent(DetaileUiEvent.DeleteFavorite(entity))
                                }

                            }, {

                                backStack()
                            })


                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 300.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(with(LocalDensity.current) { detaileInfoLayoutColumnHeight.toDp() })
                                    .padding(top = 15.dp)
                                    .padding(horizontal = 15.dp)
                                    .background(
                                        color = MidnightDark,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 30.dp)
                                    .onGloballyPositioned { layoutCoordinates ->
                                        detaileInfoLayoutColumnHeight =
                                            layoutCoordinates.size.height
                                    }
                            ) {


                                Row(modifier = Modifier.fillMaxWidth()) {

                                    GlideImage(
                                        modifier = Modifier
                                            .size(height = 220.dp, width = 145.dp),
                                        // CoilImage, FrescoImage
                                        imageModel = { detaile.poster },

                                        // shows an error text if fail to load an image.
                                        failure = {
                                            Text(text = "image request failed.")
                                        }
                                    )


                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .weight(1f)
                                            .padding(start = 6.dp)
                                            .padding(
                                                top = 20.dp
                                            ),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {

                                        Text(
                                            modifier = Modifier.padding(top = 8.dp),
                                            text = detaile.title,
                                            color = Color.White,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                        )


                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                modifier = Modifier,
                                                text = detaile.runtime.toString(),
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraLight,
                                                textAlign = TextAlign.Center,
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 1
                                            )


                                            RatingStar(detaile.imdbRating.toFloat() / 2)

                                        }


                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            text = detaile.genresListConvertToString(),
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraLight,
                                            textAlign = TextAlign.Start,
                                            overflow = TextOverflow.Ellipsis,
                                        )



                                        Row(verticalAlignment = Alignment.CenterVertically) {

                                            Card(
                                                shape = RoundedCornerShape(4.dp),
                                                colors = CardColors(
                                                    containerColor = DarkTangerine,
                                                    contentColor = DarkTangerine,
                                                    disabledContentColor = DarkTangerine,
                                                    disabledContainerColor = DarkTangerine
                                                )
                                            ) {

                                                Text(
                                                    modifier = Modifier.padding(horizontal = 4.dp),
                                                    text = IMDB,
                                                    fontFamily = ImpactFont,
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center,
                                                )

                                            }


                                            Text(
                                                modifier = Modifier.padding(horizontal = 8.dp),
                                                text = "${detaile.imdbRating}/10",
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraLight,
                                                textAlign = TextAlign.Center,
                                            )

                                        }


                                    }


                                }


                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    text = detaile.plot.toString(),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraLight,
                                    textAlign = TextAlign.Justify,
                                )


                                Text(
                                    modifier = Modifier,
                                    text = ACTORS,
                                    color = Color.LightGray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )

                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    text = detaile.actors.toString(),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraLight,
                                    textAlign = TextAlign.Justify,
                                )

                            }


                        }


                    }

                }

            }


            item {

                uiState.detaile?.images?.let { images ->


                    Column {

                        Text(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(horizontal = 30.dp),
                            text = IMAGES,
                            color = Color.LightGray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(8.dp)
                        ) {

                            items(images) { image ->

                                image?.let { image ->

                                    Card(
                                        modifier = Modifier
                                            .size(width = 230.dp, height = 180.dp)
                                            .padding(8.dp),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {


                                        GlideImage(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(EbonyClay),
                                            // CoilImage, FrescoImage
                                            imageModel = { image },

                                            loading = {

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(EbonyClay)
                                                )

                                            },

                                            // shows an error text if fail to load an image.
                                            failure = {
                                                Text(text = "image request failed.")
                                            }
                                        )

                                    }
                                }

                            }

                        }

                    }
                }
            }

        }

        uiState.errorMessage?.let { errorMessage(it) }

    }


}
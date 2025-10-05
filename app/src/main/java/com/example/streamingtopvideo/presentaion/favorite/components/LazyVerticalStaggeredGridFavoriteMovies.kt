package com.example.streamingtopvideo.presentaion.favorite.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamingtopvideo.data.db.entities.FavoriteMovieEntity
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.map


@Composable
fun LazyVerticalStaggeredGridFavoriteMovies(
    favoriteMovieEntitiesList: MutableList<FavoriteMovieEntity>,
    isAppBarVisible: (Boolean) -> Unit,
    clickItem: (Int) -> Unit
) {


    val listState = rememberLazyStaggeredGridState()

    // Detect scroll changes and update the visibility of the TopAppBar
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 0 } // Check if we are scrolling down
            .collect { isScrollingDown ->
                isAppBarVisible(!isScrollingDown) // تغییر وضعیت نمایش TopAppBar
            }
    }


    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        state = listState
    ) {

        items(favoriteMovieEntitiesList) { favoriteMovieEntity ->

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {

                        clickItem(favoriteMovieEntity.movieId)
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
                        imageModel = { favoriteMovieEntity.responseDetaile.poster },

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
                    text = favoriteMovieEntity.responseDetaile.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )


            }

        }

    }

}
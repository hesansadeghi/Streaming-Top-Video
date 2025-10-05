package com.example.streamingtopvideo.presentaion.home.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.streamingtopvideo.presentaion.ui.theme.MidnightDark
import com.valentinilk.shimmer.shimmer


@OptIn(UnstableApi::class)
@Composable
fun HomeScreenShimmer() {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightDark)
            .shimmer()
            .background(MidnightDark),
    ) {

        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(250.dp)
                .background(color = Color.LightGray, shape = CardDefaults.shape)
                .align(Alignment.CenterHorizontally)
        )


        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .size(height = 40.dp, width = 95.dp)
                .background(color = Color.LightGray, RoundedCornerShape(4.dp))
        )


        LazyRow {


            items(4) {

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(height = 44.dp, width = 85.dp)
                        .background(
                            Color.Gray, RoundedCornerShape(16.dp)
                        )
                )
            }

        }



        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .size(height = 40.dp, width = 140.dp)
                .background(color = Color.LightGray, RoundedCornerShape(4.dp))
        )

        LazyRow {

            items(3) {

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(height = 220.dp, width = 145.dp)
                        .background(
                            Color.Gray,
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                )

            }
        }

    }


}


@Composable
@Preview
fun HomeScreenShimmerPreview() {

    HomeScreenShimmer()
}
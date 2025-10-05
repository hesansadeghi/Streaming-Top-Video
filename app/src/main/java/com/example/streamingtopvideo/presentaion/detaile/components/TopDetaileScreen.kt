package com.example.streamingtopvideo.presentaion.detaile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.presentaion.playback.components.PlayBackButton
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine


@Composable
fun TopDetaileScreen(
    isFavorite: Boolean,
    setFavorite: (Boolean) -> Unit,
    backStack: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .systemBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        PlayBackButton(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape),
            resourceId = if (isFavorite) {
                R.drawable.baseline_favorite_24
            } else {

                R.drawable.baseline_favorite_border_24
            },
            description = "Favorite button",
            if (isFavorite) {
                Color.Red
            }else{
                Color.LightGray
            }
        ) {

            setFavorite(isFavorite.not())
        }



        PlayBackButton(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            resourceId = R.drawable.rounded_arrow_circle_right_24,
            description = "Back stack button",
            DarkTangerine
        ) {

            backStack()

        }


    }

}
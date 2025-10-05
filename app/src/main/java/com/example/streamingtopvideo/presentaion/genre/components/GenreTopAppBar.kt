package com.example.streamingtopvideo.presentaion.genre.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.presentaion.playback.components.PlayBackButton
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine
import com.example.streamingtopvideo.presentaion.ui.theme.MidnightDark


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreTopAppBar(title: String, backStack: () -> Unit) {


    Row(
        modifier = Modifier
            .padding(top = 18.dp)
            .padding(horizontal = 18.dp)
            .background(MidnightDark)
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            color = Color.White
        )

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
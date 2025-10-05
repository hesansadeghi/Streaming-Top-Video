package com.example.streamingtopvideo.presentaion.playback.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.streamingtopvideo.presentaion.ui.theme.DarkTangerine


@Composable
fun PlayBackCircleButton(
    modifier: Modifier = Modifier,
    @DrawableRes resourceId: Int,
    description: String,
    color: Color?,
    onClick: () -> Unit,
) {

    Image(
        modifier = modifier
            .background(color = DarkTangerine, RoundedCornerShape(100.dp))
            .padding(8.dp)
            .clickable(onClick = onClick),
        contentDescription = description,
        painter = painterResource(resourceId),
        colorFilter = color?.let { ColorFilter.tint(color) }
    )

}
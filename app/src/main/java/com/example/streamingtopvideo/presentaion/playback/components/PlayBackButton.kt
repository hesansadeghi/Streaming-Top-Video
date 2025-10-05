package com.example.streamingtopvideo.presentaion.playback.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource


@Composable
fun PlayBackButton(
    modifier: Modifier = Modifier,
    @DrawableRes resourceId: Int,
    description: String,
    color: Color?,
    onClick: () -> Unit,
) {

    Image(
        modifier = modifier
            .clickable(onClick = onClick),
        contentDescription = description,
        painter = painterResource(resourceId),
        colorFilter = color?.let { ColorFilter.tint(color) }
    )

}

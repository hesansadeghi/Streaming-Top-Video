package com.example.streamingtopvideo.models

import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.util.Constants.DOWNLOADS
import com.example.streamingtopvideo.util.Constants.FAVORITE
import com.example.streamingtopvideo.util.Constants.HOME

sealed class NavigationItem(var route: String, var title: String, var icon: Int) {

    data object Home : NavigationItem(HOME, "Home", R.drawable.icons8_home)
    data object Favorite : NavigationItem(FAVORITE, "Favorites", R.drawable.baseline_favorite_24)
    data object Downloads : NavigationItem(DOWNLOADS, "Downloads", R.drawable.outline_download_24)

}
package com.example.streamingtopvideo.presentaion.home

import com.example.streamingtopvideo.R
import com.example.streamingtopvideo.data.models.ResponseGenresList
import com.example.streamingtopvideo.data.models.ResponseMoviesList
import java.io.Serializable

data class HomeUiState(
    val streamingMovies: List<StreamingMovie> = listOf(
        StreamingMovie(
            1,
            R.drawable.big_buck_bunny_animation,
            "Big Buck Bunny (animation)",
            "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd",
            "2.25 GB"
        ),
        StreamingMovie(
            2,
            R.drawable.tears_of_steel,
            "Tears of Steel",
            "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd",
            "2.41 GB"
        ),
        StreamingMovie(
            3,
            R.drawable.sintel_poster,
            "Sintel (animation)",
            "https://storage.googleapis.com/shaka-demo-assets/sintel/dash.mpd",
            "5.87 GB"
        ),
//        StreamingMovie(
//            4,
//            R.drawable.baseline_favorite_24,
//            "test",
//            "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"
//        )
    ),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val moviesList: ResponseMoviesList? = null,
    val musicList: ResponseMoviesList? = null,
    val fantasyMoviesList: ResponseMoviesList? = null,
    val genresList: ResponseGenresList? = null
)


data class StreamingMovie(
    val id: Int,
    val coverImageResourceId: Int,
    val title: String,
    val streamUrl: String,
    var movieSize : String
) : Serializable

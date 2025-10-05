package com.example.streamingtopvideo.data.models


import com.google.gson.annotations.SerializedName

class ResponseGenresList : ArrayList<ResponseGenresList.ResponseGenresListItem>(){
    data class ResponseGenresListItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}
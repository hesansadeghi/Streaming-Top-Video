package com.example.streamingtopvideo.data.db

import androidx.room.TypeConverter
import com.example.streamingtopvideo.data.models.ResponseDetaile
import com.google.gson.Gson

class TypeConvertorCustom {

    private val gson = Gson()

    @TypeConverter
    fun responseDetaileToJson(responseDetaile: ResponseDetaile): String {

        return gson.toJson(responseDetaile)
    }

    @TypeConverter
    fun stringToResponseDetaile(string: String): ResponseDetaile {

        return gson.fromJson(string,ResponseDetaile::class.java)
    }


}
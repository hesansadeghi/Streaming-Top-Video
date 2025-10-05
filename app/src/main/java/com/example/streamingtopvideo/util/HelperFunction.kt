package com.example.streamingtopvideo.util

import retrofit2.Response

inline fun <T> safeApiCall(apiCall: () -> Response<T>): Resource<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(response.message())
        }
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Unknown error occurred")
    }
}
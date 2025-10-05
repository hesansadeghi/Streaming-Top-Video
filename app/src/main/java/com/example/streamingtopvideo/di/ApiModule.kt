package com.example.streamingtopvideo.di

import com.example.streamingtopvideo.data.api.ApiService
import com.example.streamingtopvideo.data.repositories.HomeRepository
import com.example.streamingtopvideo.data.uses_cases.home.GetExistsDownloadedMovieCase
import com.example.streamingtopvideo.data.uses_cases.home.GetFantasyMovieWithGenre
import com.example.streamingtopvideo.data.uses_cases.home.GetGenresCase
import com.example.streamingtopvideo.data.uses_cases.home.GetMusicWithGenre
import com.example.streamingtopvideo.data.uses_cases.home.HomeUseCase
import com.example.streamingtopvideo.data.uses_cases.home.LastMoviesCase
import com.example.streamingtopvideo.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideBaseUrl()=Constants.BASE_URL


    @Provides
    @Singleton
    fun provideTimeConnection()=Constants.CONNECTION_TIME


    @Provides
    @Singleton
    fun provideGson()=GsonBuilder().create()


    @Provides
    @Singleton
    fun provideInterCeptor()=HttpLoggingInterceptor().apply {

        level=HttpLoggingInterceptor.Level.BODY
    }



    @Provides
    @Singleton
    fun provideGetClient(time:Long,inceptor:HttpLoggingInterceptor)=OkHttpClient.Builder()
        .addInterceptor(inceptor)
        .writeTimeout(time,TimeUnit.SECONDS)
        .readTimeout(time,TimeUnit.SECONDS)
        .connectTimeout(time,TimeUnit.SECONDS)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl :String,gson: Gson,client :OkHttpClient) :ApiService=Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ApiService::class.java)



}
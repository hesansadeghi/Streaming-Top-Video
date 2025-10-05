package com.example.streamingtopvideo.di

import android.app.Application
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.streamingtopvideo.util.DownloadManagerUtil
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ExoPlayerModule {

//    @Provides
//    @Singleton
//    fun provideExoPlayerManager(@ApplicationContext application: Application): ExoPlayerManager {
//        return ExoPlayerManager(application)
//    }

}

package com.example.streamingtopvideo.data.repositories

import com.example.streamingtopvideo.data.db.dao.DownloadedMoviesDao
import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.data.repositories.interfaces.DeleteDownloadedMovieRepository
import com.example.streamingtopvideo.data.repositories.interfaces.GetAllDownloadedMoviesRepository
import jakarta.inject.Inject

class DownloadsRepository @Inject constructor(private val dao: DownloadedMoviesDao) :
    GetAllDownloadedMoviesRepository,
    DeleteDownloadedMovieRepository {
    override suspend fun getAllDownloadedMovies(): MutableList<DownloadedMovieEntity> {
        return dao.getAllDownloadedMovies()
    }

    override suspend fun deleteDownloadedMovie(entity: DownloadedMovieEntity) {
        dao.deleteDownloadedMovie(entity)
    }
}
package com.example.streamingtopvideo.di

import com.example.streamingtopvideo.data.repositories.DetaileRepository
import com.example.streamingtopvideo.data.repositories.DownloadsRepository
import com.example.streamingtopvideo.data.repositories.FavoriteRepository
import com.example.streamingtopvideo.data.repositories.GenreRepository
import com.example.streamingtopvideo.data.repositories.HomeRepository
import com.example.streamingtopvideo.data.repositories.SearchRepository
import com.example.streamingtopvideo.data.uses_cases.detaile.DeleteFavoriteMovieCase
import com.example.streamingtopvideo.data.uses_cases.detaile.DetaileUseCase
import com.example.streamingtopvideo.data.uses_cases.detaile.GetDetaileMovieCase
import com.example.streamingtopvideo.data.uses_cases.detaile.GetExistFavoriteMovie
import com.example.streamingtopvideo.data.uses_cases.detaile.GetFavoriteMovieDetaileCase
import com.example.streamingtopvideo.data.uses_cases.detaile.InsertFavoriteMovieCase
import com.example.streamingtopvideo.data.uses_cases.downloads.DeleteDownloadedMovieUseCase
import com.example.streamingtopvideo.data.uses_cases.downloads.DownloadsUseCase
import com.example.streamingtopvideo.data.uses_cases.downloads.GetAllDownloadedMoviesCase
import com.example.streamingtopvideo.data.uses_cases.favorite.FavoriteUseCase
import com.example.streamingtopvideo.data.uses_cases.favorite.GetAllFavoriteMovieCase
import com.example.streamingtopvideo.data.uses_cases.genre.GenreUseCase
import com.example.streamingtopvideo.data.uses_cases.genre.GetMoviesWithGenre
import com.example.streamingtopvideo.data.uses_cases.home.GetExistsDownloadedMovieCase
import com.example.streamingtopvideo.data.uses_cases.home.GetFantasyMovieWithGenre
import com.example.streamingtopvideo.data.uses_cases.home.GetGenresCase
import com.example.streamingtopvideo.data.uses_cases.home.GetMusicWithGenre
import com.example.streamingtopvideo.data.uses_cases.home.HomeUseCase
import com.example.streamingtopvideo.data.uses_cases.home.InsertDownloadedMovieCase
import com.example.streamingtopvideo.data.uses_cases.home.LastMoviesCase
import com.example.streamingtopvideo.data.uses_cases.search.GetSearchMoviesByQueryCase
import com.example.streamingtopvideo.data.uses_cases.search.SearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideHomeUseCase(repository: HomeRepository): HomeUseCase {

        return HomeUseCase(
            lastMoviesCase = LastMoviesCase(repository),
            getGenresCase = GetGenresCase(repository),
            getMusicWithGenre = GetMusicWithGenre(repository),
            getFantasyMovieWithGenre = GetFantasyMovieWithGenre(repository),
            getExistsDownloadedMovieCase = GetExistsDownloadedMovieCase(repository),
            insertDownloadedMovieCase = InsertDownloadedMovieCase(repository)
        )
    }


    @Provides
    @Singleton
    fun provideDownloadsUseCase(repository: DownloadsRepository): DownloadsUseCase {

        return DownloadsUseCase(
            getAllDownloadedMoviesCase = GetAllDownloadedMoviesCase(repository),
            deleteDownloadedMovieUseCase = DeleteDownloadedMovieUseCase(repository)
        )
    }


    @Provides
    @Singleton
    fun provideDetaileUseCase(repository: DetaileRepository): DetaileUseCase {

        return DetaileUseCase(
            getDetaileMovieCase = GetDetaileMovieCase(repository),
            getExistFavoriteMovie = GetExistFavoriteMovie(repository),
            insertFavoriteMovieCase = InsertFavoriteMovieCase(repository),
            deleteFavoriteMovieCase = DeleteFavoriteMovieCase(repository),
            getFavoriteMovieDetaileCase = GetFavoriteMovieDetaileCase(repository)
        )
    }


    @Provides
    @Singleton
    fun provideFavoriteUseCase(repository: FavoriteRepository): FavoriteUseCase {

        return FavoriteUseCase(GetAllFavoriteMovieCase(repository))
    }


    @Provides
    @Singleton
    fun provideGenreUseCase(repository: GenreRepository): GenreUseCase {

        return GenreUseCase(GetMoviesWithGenre(repository))
    }


    @Provides
    @Singleton
    fun provideSearchUseCase(repository: SearchRepository): SearchUseCase {

        return SearchUseCase(GetSearchMoviesByQueryCase(repository))
    }

}
package com.example.streamingtopvideo.presentaion.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import com.example.streamingtopvideo.data.db.entities.DownloadedMovieEntity
import com.example.streamingtopvideo.data.models.ResponseGenresList.ResponseGenresListItem
import com.example.streamingtopvideo.data.uses_cases.home.HomeUseCase
import com.example.streamingtopvideo.models.DownloadUiModel
import com.example.streamingtopvideo.models.PlayMovieModel
import com.example.streamingtopvideo.service.DownloadMovieService
import com.example.streamingtopvideo.util.Constants.CANCEL_DOWNLOAD
import com.example.streamingtopvideo.util.Constants.CANCEL_REQUEST_ID
import com.example.streamingtopvideo.util.Constants.DOWNLOAD_BYTE
import com.example.streamingtopvideo.util.Constants.DOWNLOAD_PERCENT
import com.example.streamingtopvideo.util.Constants.DOWNLOAD_STATE
import com.example.streamingtopvideo.util.Constants.DOWNLOAD_UPDATE
import com.example.streamingtopvideo.util.Constants.MOVIE_MODEL
import com.example.streamingtopvideo.util.Constants.REQUEST_ID
import com.example.streamingtopvideo.util.Constants.START_DOWNLOAD
import com.example.streamingtopvideo.util.DownloadManagerUtil
import com.example.streamingtopvideo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@UnstableApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()


    private val _playMovie = MutableStateFlow<PlayMovieModel?>(null)
    val playMovie: StateFlow<PlayMovieModel?> = _playMovie


    private val _detaileMovieNavigate = MutableStateFlow<Int?>(null)
    val detaileMovieNavigate: StateFlow<Int?> = _detaileMovieNavigate


    private val _moviesGenreNavigate = MutableStateFlow<ResponseGenresListItem?>(null)
    val moviesGenreNavigate: StateFlow<ResponseGenresListItem?> = _moviesGenreNavigate


    private val _downloads = MutableStateFlow<Map<String, DownloadUiModel>>(emptyMap())
    val downloads: StateFlow<Map<String, DownloadUiModel>> = _downloads


    private val downloadUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == DOWNLOAD_UPDATE) {
                val id = intent.getStringExtra(REQUEST_ID) ?: return
                val state = intent.getIntExtra(DOWNLOAD_STATE, Download.STATE_STOPPED)
                val percentDownloaded = intent.getFloatExtra(DOWNLOAD_PERCENT, 0f)
                val byteDownloaded = intent.getLongExtra(DOWNLOAD_BYTE, 0L)

                viewModelScope.launch {
                    _downloads.value = _downloads.value.toMutableMap().apply {
                        this[id] = DownloadUiModel(
                            id = id,
                            state = state,
                            percentDownloaded = percentDownloaded,
                            bytesDownloaded = byteDownloaded
                        )
                    }
                }
            }
        }
    }

    fun registerReceiver() {
        val filter = IntentFilter(DOWNLOAD_UPDATE)
        ContextCompat.registerReceiver(
            context,
            downloadUpdateReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(downloadUpdateReceiver)
    }


    init {

        onEvent(HomeUiEvent.GetAllData)
        onEvent(HomeUiEvent.LoadExistingDownloads)

        registerReceiver()

    }


    private fun loadExistingDownloads() {
        viewModelScope.launch {
            val manager = DownloadManagerUtil.getDownloadManager(context)
            val cursor = manager.downloadIndex.getDownloads()
            val downloads = mutableMapOf<String, DownloadUiModel>()

            cursor.use {
                while (it.moveToNext()) {

                    val download = it.download

                    downloads[download.request.id] = DownloadUiModel(
                        download.request.id,
                        download.state,
                        download.percentDownloaded,
                        download.bytesDownloaded
                    )
                }
            }

            _downloads.value = downloads
        }
    }


    fun onEvent(event: HomeUiEvent) {

        when (event) {
            is HomeUiEvent.ClickedItemGenre -> genreMovie(event.genre)
            is HomeUiEvent.ClickedItemMovie -> detaileMovie(event.movieId)
            is HomeUiEvent.DownloadMovie -> {
                downLoadMovie(event.streamingMovie)
            }

            is HomeUiEvent.GetAllData -> loadHomeData()
            is HomeUiEvent.PlayMovie -> playMovie(
                PlayMovieModel(
                    event.id,
                    event.url,
                    event.titleMovie
                )
            )

            is HomeUiEvent.LoadExistingDownloads -> loadExistingDownloads()
            is HomeUiEvent.CheckDownloadMovieServiceIsRunning -> checkDownloadMovieServiceIsRunning(
                event.streamingMovie
            )

            is HomeUiEvent.CheckExistAndInsertDownloadedMovie -> checkExistAndInsertDownloadedMovie(
                event.movieId, event.entity
            )

            is HomeUiEvent.CancelDownloadMovie -> cancelDownLoadMovie(event.downloadId)
        }

    }

    private fun playMovie(playMovieModel: PlayMovieModel) {

        _playMovie.value = playMovieModel
    }

    private fun detaileMovie(movieId: Int?) = viewModelScope.launch {

        _detaileMovieNavigate.value = movieId
    }

    private fun genreMovie(genre: ResponseGenresListItem?) = viewModelScope.launch {

        _moviesGenreNavigate.value = genre
    }

    private fun checkExistAndInsertDownloadedMovie(movieId: String, entity: DownloadedMovieEntity) =
        viewModelScope.launch {

            val existsDownloadedMovieDeferred =
                async { homeUseCase.getExistsDownloadedMovieCase(movieId) }

            val isExistsDownloadedMovieResult = existsDownloadedMovieDeferred.await()

            if (isExistsDownloadedMovieResult.not()) {

                async { homeUseCase.insertDownloadedMovieCase(entity) }
            }

        }


    private fun loadHomeData() = viewModelScope.launch {

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        try {

            val lastMoviesDeferred = async { homeUseCase.lastMoviesCase() }
            val genresDeferred = async { homeUseCase.getGenresCase() }
            val musicDeferred = async { homeUseCase.getMusicWithGenre() }
            val fantasyMovieDeferred = async { homeUseCase.getFantasyMovieWithGenre() }

            val lastMoviesResult = lastMoviesDeferred.await()
            val genresResult = genresDeferred.await()
            val musicResult = musicDeferred.await()
            val fantasyMovieResult = fantasyMovieDeferred.await()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                moviesList = if (lastMoviesResult is Resource.Success) lastMoviesResult.data else null,
                genresList = if (genresResult is Resource.Success) genresResult.data else null,
                musicList = if (musicResult is Resource.Success) musicResult.data else null,
                fantasyMoviesList = if (fantasyMovieResult is Resource.Success) fantasyMovieResult.data else null,
                errorMessage = if (genresResult is Resource.Error) genresResult.message else if (lastMoviesResult is Resource.Error) lastMoviesResult.message else null
            )


        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = e.message ?: "Unknown error"
            )
        }

    }

    private fun downLoadMovie(movie: StreamingMovie) {
        _downloads.value = _downloads.value.toMutableMap().apply {

            this[movie.id.toString()] = DownloadUiModel(
                movie.id.toString(),
                Download.STATE_QUEUED,
                0f,
                0L
            )
        }

        val intent = Intent(context, DownloadMovieService::class.java).apply {
            putExtra(MOVIE_MODEL, movie)
            action = START_DOWNLOAD
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

    }


    private fun cancelDownLoadMovie(downloadId: String) {

        val cancelIntent = Intent(context, DownloadMovieService::class.java).apply {
            action = CANCEL_DOWNLOAD
            putExtra(CANCEL_REQUEST_ID, downloadId)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(cancelIntent)
        } else {
            context.startService(cancelIntent)
        }
    }


    private fun checkDownloadMovieServiceIsRunning(streamingMovie: StreamingMovie) {

        if (DownloadMovieService.isRunning.not()) {

            downLoadMovie(streamingMovie)
        }
    }


    override fun onCleared() {
        super.onCleared()

        unregisterReceiver()
    }
}

package com.example.streamingtopvideo.presentaion.playback

import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import com.example.streamingtopvideo.util.ExoPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@UnstableApi
@HiltViewModel
class PlayBackViewModel @Inject constructor(
    private val exoPlayerManager: ExoPlayerManager
) :
    ViewModel() {

    private val _playerUiModel = MutableStateFlow(PlayerUiModel())
    val playerUiModel = _playerUiModel.asStateFlow()


    init {

        exoPlayerManagerListeners()
    }


    private fun exoPlayerManagerListeners() {

        exoPlayerManager.apply {


            setOnVideoAspectRatioListener { videoAspectRatio ->

                _playerUiModel.value = _playerUiModel.value.copy(
                    videoAspectRatio = videoAspectRatio
                )
            }

            setOnPlaybackStateListener { playbackState ->

                _playerUiModel.value = _playerUiModel.value.copy(
                    playbackState = playbackState
                )
            }

            setOnCurrentSubtitlesListener { currentSubtitles ->

                _playerUiModel.value = _playerUiModel.value.copy(

                    currentSubtitles = currentSubtitles
                )
            }

            setOnShowPlayerControlsListener {

                showPlayerControls()
            }

            setOnTrackSelectionUiModelListener { trackSelectionUiModel ->

                _playerUiModel.value = _playerUiModel.value.copy(

                    trackSelectionUiModel = trackSelectionUiModel
                )
            }

            setOnTimelineUiModelListener { timelineUiModel ->

                val newTimelineUiModel = timelineUiModel ?: _playerUiModel.value.timelineUiModel

                _playerUiModel.value = _playerUiModel.value.copy(
                    timelineUiModel = newTimelineUiModel,
                )
            }

            setOnVideoTrackListener { videoTrack ->

                _playerUiModel.value = _playerUiModel.value.copy(
                    trackSelectionUiModel = _playerUiModel.value.trackSelectionUiModel?.copy(
                        selectedVideoTrack = videoTrack
                    )
                )
            }


            setOnAudioTrackListener { audioTrack ->

                _playerUiModel.value = _playerUiModel.value.copy(
                    trackSelectionUiModel = _playerUiModel.value.trackSelectionUiModel?.copy(
                        selectedAudioTrack = audioTrack
                    )
                )
            }


            setOnSubtitleTrackListener { subtitleTrack ->

                _playerUiModel.value = _playerUiModel.value.copy(
                    trackSelectionUiModel = _playerUiModel.value.trackSelectionUiModel?.copy(
                        selectedSubtitleTrack = subtitleTrack
                    )
                )
            }


            setOnPlaybackSpeedListener { videoSpeed ->

                _playerUiModel.value = _playerUiModel.value.copy(
                    videoSpeedUiModel = _playerUiModel.value.videoSpeedUiModel?.copy(
                        selectedSpeed = videoSpeed
                    )
                )

            }


        }

    }


    fun openTrackSelector() {

        _playerUiModel.value = _playerUiModel.value.copy(
            isTrackSelectorVisible = true
        )
    }


    fun hideTrackSelector() {

        _playerUiModel.value = _playerUiModel.value.copy(
            isTrackSelectorVisible = false
        )
    }


    fun openVideoSpeedSelector() {

        _playerUiModel.value = _playerUiModel.value.copy(
            isVideoSpeedVisible = true
        )
    }


    fun hideVideoSpeedSelector() {

        _playerUiModel.value = _playerUiModel.value.copy(
            isVideoSpeedVisible = false
        )
    }


    fun showPlayerControls() {

        _playerUiModel.value = _playerUiModel.value.copy(
            playerControlsVisible = true
        )

    }


    fun hidePlayerControls() {

        _playerUiModel.value = _playerUiModel.value.copy(
            playerControlsVisible = false
        )
    }


    private fun popBackStack() {
        exoPlayerManager.stop()
        _playerUiModel.value = _playerUiModel.value.copy(
            popBackStack = true
        )
    }


    private fun initMovieTitle(movieTitle: String) {

        _playerUiModel.value = _playerUiModel.value.copy(
            titleMovie = movieTitle
        )
    }


    fun onEvent(playerUiEvent: PlayerUiEvent) {

        when (playerUiEvent) {
            is AttachSurface -> {

                exoPlayerManager.setVideoSurface(playerUiEvent.surface)
            }

            DetachSurface -> {

                exoPlayerManager.setVideoSurface(null)
            }

            is FastForward -> {

                exoPlayerManager.seekToPosition(
                    exoPlayerManager.currentPosition + playerUiEvent.amountInMs
                )
            }

            is Init -> {

                exoPlayerManager.initUrl(playerUiEvent.streamUrl, playerUiEvent.movieId)
                initMovieTitle(playerUiEvent.movieTitle)
            }

            Pause -> {

                exoPlayerManager.pause()
            }

            Resume -> {

                exoPlayerManager.play()
            }

            is Rewind -> {

                if (playerUiEvent.amountInMs > 10) {

                    exoPlayerManager.seekToPosition(
                        exoPlayerManager.currentPosition - playerUiEvent.amountInMs
                    )
                } else {

                    exoPlayerManager.seekToPosition(0)
                }
            }

            is Seek -> {

                exoPlayerManager.seekToPosition(playerUiEvent.targetInMs)
            }

            is Start -> {

                exoPlayerManager.start(playerUiEvent.positionInMs)
            }

            Stop -> {

                exoPlayerManager.stop()
            }

            is SetAudioTrack -> {
                exoPlayerManager.setAudioTrack(playerUiEvent.track)
            }

            is SetSubtitleTrack -> {
                exoPlayerManager.setSubtitleTrack(playerUiEvent.track)
            }

            is SetVideoTrack -> {
                exoPlayerManager.setVideoTrack(playerUiEvent.track)
            }

            is SetPlaybackParameters -> {

                exoPlayerManager.setPlaybackParameters(playerUiEvent.speed)
            }

            is BackStack -> {

                popBackStack()
            }
        }

    }

}
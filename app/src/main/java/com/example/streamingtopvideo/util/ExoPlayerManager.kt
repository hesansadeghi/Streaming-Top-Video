package com.example.streamingtopvideo.util

import android.content.Context
import android.view.Surface
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem.Builder
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.Cue
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.streamingtopvideo.presentaion.playback.AudioTrack
import com.example.streamingtopvideo.presentaion.playback.PlaybackState
import com.example.streamingtopvideo.presentaion.playback.SubtitleTrack
import com.example.streamingtopvideo.presentaion.playback.TimelineUiModel
import com.example.streamingtopvideo.presentaion.playback.TrackSelectionUiModel
import com.example.streamingtopvideo.presentaion.playback.VideoTrack
import com.google.common.collect.ImmutableList
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExoPlayerManager @Inject constructor(@param:ApplicationContext private val context: Context) {

    private val playerEvenListener: Player.Listener = object : Player.Listener {
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            if (videoSize !== VideoSize.UNKNOWN) {
                val videoWidth = videoSize.width
                val videoHeight = videoSize.height / videoSize.pixelWidthHeightRatio
                val videoAspectRatio = videoWidth / videoHeight

                videoAspectRatioListener?.let {

                    it(videoAspectRatio)
                }

            }
        }


        override fun onIsPlayingChanged(isPlaying: Boolean) {

            if (isPlaying) {


                playbackStateListener?.let {

                    it(PlaybackState.PLAYING)
                }

            } else if (exoPlayer.playbackState == Player.STATE_READY) {


                playbackStateListener?.let {

                    it(PlaybackState.PAUSE)
                }

            }
        }


        override fun onCues(cueGroup: CueGroup) {

            currentSubtitlesListener?.let {

                it(cueGroup.cues)
            }

        }

        override fun onPlaybackStateChanged(playbackState: Int) {

            val state = when (playbackState) {

                Player.STATE_BUFFERING -> PlaybackState.BUFFERING

                Player.STATE_ENDED -> PlaybackState.COMPLETED

                Player.STATE_IDLE -> {

                    if (exoPlayer.playerError != null) {

                        PlaybackState.ERROR
                    } else {

                        PlaybackState.IDLE
                    }
                }

                Player.STATE_READY -> {

                    if (exoPlayer.playWhenReady) {

                        PlaybackState.PLAYING
                    } else {

                        PlaybackState.PAUSE
                    }

                }

                else -> PlaybackState.IDLE
            }

            playbackStateListener?.let {

                it(state)
            }


            if (state == PlaybackState.ERROR) {

                showPlayerControlsListener?.let {

                    it()
                }


            }

            when (playbackState) {

                Player.STATE_READY -> {

                    startTrackingPlaybackPosition()
                }

                else -> {

                    stopTrackingPlaybackPosition()
                }
            }

        }


        override fun onTracksChanged(tracks: Tracks) {

            val newVideoTracks = mutableMapOf<VideoTrack, ExoPlayerTrack?>(
                VideoTrack.Companion.AUTO to null
            )

            val newAudioTracks = mutableMapOf<AudioTrack, ExoPlayerTrack?>(
                AudioTrack.Companion.AUTO to null,
                AudioTrack.Companion.NONE to null
            )

            val newSubtitleTracks = mutableMapOf<SubtitleTrack, ExoPlayerTrack?>(
                SubtitleTrack.Companion.AUTO to null,
                SubtitleTrack.Companion.NONE to null
            )


            tracks.groups.forEach { it ->

                when (it.type) {

                    C.TRACK_TYPE_AUDIO -> {
                        newAudioTracks.putAll(extractAudioTracks(it))
                    }

                    C.TRACK_TYPE_VIDEO -> {
                        newVideoTracks.putAll(extractVideoTracks(it))
                    }

                    C.TRACK_TYPE_TEXT -> {
                        newSubtitleTracks.putAll(extractSubtitleTracks(it))
                    }

                }


                videoTrackMaps = newVideoTracks
                audioTrackMaps = newAudioTracks
                subtitleTrackMaps = newSubtitleTracks


                trackSelectionUiModelListener?.let {

                    it(
                        TrackSelectionUiModel(
                            selectedVideoTrack = selectedVideoTrack,
                            videoTracks = videoTrackMaps.keys.toList(),
                            selectedAudioTrack = selectedAudioTrack,
                            audioTracks = audioTrackMaps.keys.toList(),
                            selectedSubtitleTrack = selectedSubtitleTrack,
                            subtitleTracks = subtitleTrackMaps.keys.toList()
                        )
                    )
                }


            }

        }


        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

            val speed = playbackParameters.speed

            playbackSpeedListener?.let {
                it(speed)
            }

        }

        private fun startTrackingPlaybackPosition() {

            positionTrackingJob = playerCoroutineScope.launch {
                while (true) {
                    val newTimelineUiModel = buildTimelineUiModel()

                    newTimelineUiModel.let { newTimelineUiModel ->

                        timelineUiModelListener?.let {

                            it(newTimelineUiModel)
                        }
                    }

                    delay(1000)
                }


            }
        }

        private fun stopTrackingPlaybackPosition() {

            buildTimelineUiModel()
            positionTrackingJob?.cancel()
            positionTrackingJob = null
        }


        private fun buildTimelineUiModel(): TimelineUiModel? {

            val duration = exoPlayer.contentDuration
            if (duration == C.TIME_UNSET) return null
            currentPosition = exoPlayer.currentPosition
            val bufferedPosition = exoPlayer.contentBufferedPosition
            return TimelineUiModel(
                durationInMs = duration,
                currentPositionInMs = currentPosition  ,
                bufferedPositionInMs = bufferedPosition
            )
        }


    }


    private var exoPlayer = buildExoPlayer(context, ThisApp.isLocaleMovie).apply {

        addListener(playerEvenListener)
    }


    @OptIn(UnstableApi::class)
    private fun buildExoPlayer(context: Context, isLocale: Boolean): ExoPlayer {

        return if (isLocale) {

            ExoPlayer.Builder(context)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(DownloadManagerUtil.buildDataSourceFactory(context))
                ).build()

        } else {

            ExoPlayer.Builder(context).build()
        }
    }

    var currentPosition = exoPlayer.currentPosition

    private val playerCoroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private var positionTrackingJob: Job? = null

    private var selectedVideoTrack: VideoTrack = VideoTrack.Companion.AUTO
    private var selectedAudioTrack: AudioTrack = AudioTrack.Companion.AUTO
    private var selectedSubtitleTrack: SubtitleTrack = SubtitleTrack.Companion.AUTO
    private var videoTrackMaps: Map<VideoTrack, ExoPlayerTrack?> = emptyMap()
    private var audioTrackMaps: Map<AudioTrack, ExoPlayerTrack?> = emptyMap()
    private var subtitleTrackMaps: Map<SubtitleTrack, ExoPlayerTrack?> = emptyMap()


    private var videoAspectRatioListener: ((Float) -> Unit)? = null
    fun setOnVideoAspectRatioListener(listener: (Float) -> Unit) {

        videoAspectRatioListener = listener

    }

    private var playbackStateListener: ((PlaybackState) -> Unit)? = null
    fun setOnPlaybackStateListener(listener: (PlaybackState) -> Unit) {

        playbackStateListener = listener
    }

    private var currentSubtitlesListener: ((ImmutableList<Cue>) -> Unit)? = null
    fun setOnCurrentSubtitlesListener(listener: (ImmutableList<Cue>) -> Unit) {

        currentSubtitlesListener = listener
    }


    private var showPlayerControlsListener: (() -> Unit)? = null
    fun setOnShowPlayerControlsListener(listener: () -> Unit) {

        showPlayerControlsListener = listener
    }


    private var trackSelectionUiModelListener: ((TrackSelectionUiModel) -> Unit)? = null
    fun setOnTrackSelectionUiModelListener(listener: (TrackSelectionUiModel) -> Unit) {

        trackSelectionUiModelListener = listener
    }

    private var timelineUiModelListener: ((TimelineUiModel?) -> Unit)? = null
    fun setOnTimelineUiModelListener(listener: (TimelineUiModel?) -> Unit) {

        timelineUiModelListener = listener
    }

    private var videoTrackListener: ((VideoTrack) -> Unit)? = null
    fun setOnVideoTrackListener(listener: (VideoTrack) -> Unit) {

        videoTrackListener = listener
    }


    private var audioTrackListener: ((AudioTrack) -> Unit)? = null
    fun setOnAudioTrackListener(listener: (AudioTrack) -> Unit) {

        audioTrackListener = listener
    }


    private var subtitleTrackListener: ((SubtitleTrack) -> Unit)? = null
    fun setOnSubtitleTrackListener(listener: (SubtitleTrack) -> Unit) {

        subtitleTrackListener = listener
    }


    private var playbackSpeedListener: ((Float) -> Unit)? = null
    fun setOnPlaybackSpeedListener(listener: (Float) -> Unit) {

        playbackSpeedListener = listener
    }


    class ExoPlayerTrack(
        val trackGroup: TrackGroup,
        val trackIndexInGroup: Int
    )


    @OptIn(UnstableApi::class)
    private fun extractAudioTracks(info: Tracks.Group): Map<AudioTrack, ExoPlayerTrack> {

        val result = mutableMapOf<AudioTrack, ExoPlayerTrack>()
        for (trackIndex in 0 until info.mediaTrackGroup.length) {

            if (info.isTrackSupported(trackIndex)) {

                val format = info.mediaTrackGroup.getFormat(trackIndex)
                val language = format.language

                if (language != null) {
                    val audioTrack = AudioTrack(language)
                    result[audioTrack] = ExoPlayerTrack(
                        trackGroup = info.mediaTrackGroup,
                        trackIndexInGroup = trackIndex
                    )
                }
            }
        }
        return result
    }


    @OptIn(UnstableApi::class)
    private fun extractVideoTracks(info: Tracks.Group): Map<VideoTrack, ExoPlayerTrack> {

        val result = mutableMapOf<VideoTrack, ExoPlayerTrack>()
        for (trackIndex in 0 until info.mediaTrackGroup.length) {

            if (info.isTrackSupported(trackIndex)) {

                val format = info.mediaTrackGroup.getFormat(trackIndex)
                val width = format.width
                val height = format.height
                val videoTrack = VideoTrack(width, height)
                result[videoTrack] = ExoPlayerTrack(
                    trackGroup = info.mediaTrackGroup,
                    trackIndexInGroup = trackIndex
                )
            }
        }
        return result
    }


    @OptIn(UnstableApi::class)
    private fun extractSubtitleTracks(info: Tracks.Group): Map<SubtitleTrack, ExoPlayerTrack> {

        val result = mutableMapOf<SubtitleTrack, ExoPlayerTrack>()
        for (trackIndex in 0 until info.mediaTrackGroup.length) {

            if (info.isTrackSupported(trackIndex)) {

                val format = info.mediaTrackGroup.getFormat(trackIndex)
                val language = format.language

                if (language != null) {
                    val subtitleTrack = SubtitleTrack(language)
                    result[subtitleTrack] = ExoPlayerTrack(
                        trackGroup = info.mediaTrackGroup,
                        trackIndexInGroup = trackIndex
                    )
                }
            }
        }
        return result
    }


    fun setVideoTrack(track: VideoTrack) {

        val selectionBuilder = exoPlayer.trackSelectionParameters.buildUpon()
            .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
        when {

            track == VideoTrack.Companion.AUTO -> {
                selectedVideoTrack = track
            }

            else -> {
                val exoVideoTrack = videoTrackMaps[track]
                if (exoVideoTrack != null) {
                    selectionBuilder.setOverrideForType(
                        TrackSelectionOverride(
                            exoVideoTrack.trackGroup,
                            listOf(exoVideoTrack.trackIndexInGroup)
                        )
                    )

                    selectedVideoTrack = track
                }
            }
        }
        exoPlayer.trackSelectionParameters = selectionBuilder.build()

        videoTrackListener?.let {

            it(track)
        }

    }


    fun setAudioTrack(track: AudioTrack) {

        val trackDisable = track == AudioTrack.Companion.NONE
        val selectionBuilder = exoPlayer.trackSelectionParameters.buildUpon()
            .clearOverridesOfType(C.TRACK_TYPE_AUDIO)
            .setTrackTypeDisabled(C.TRACK_TYPE_AUDIO, trackDisable)
        when {

            track === AudioTrack.Companion.AUTO || track === AudioTrack.Companion.NONE -> {
                selectedAudioTrack = track
            }

            else -> {
                val exoAudioTrack = audioTrackMaps[track]
                if (exoAudioTrack != null) {
                    selectionBuilder.setOverrideForType(
                        TrackSelectionOverride(
                            exoAudioTrack.trackGroup,
                            listOf(exoAudioTrack.trackIndexInGroup)
                        )
                    )

                    selectedAudioTrack = track
                }
            }
        }
        exoPlayer.trackSelectionParameters = selectionBuilder.build()

        audioTrackListener?.let {

            it(track)
        }

    }


    fun setSubtitleTrack(track: SubtitleTrack) {

        val trackDisable = track == SubtitleTrack.Companion.NONE
        val selectionBuilder = exoPlayer.trackSelectionParameters.buildUpon()
            .clearOverridesOfType(C.TRACK_TYPE_TEXT)
            .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, trackDisable)
        when {

            track === SubtitleTrack.Companion.AUTO || track === SubtitleTrack.Companion.NONE -> {
                selectedSubtitleTrack = track
            }

            else -> {
                val exoSubtitleTrack = subtitleTrackMaps[track]
                if (exoSubtitleTrack != null) {
                    selectionBuilder.setOverrideForType(
                        TrackSelectionOverride(
                            exoSubtitleTrack.trackGroup,
                            listOf(exoSubtitleTrack.trackIndexInGroup)
                        )
                    )

                    selectedSubtitleTrack = track
                }
            }
        }
        exoPlayer.trackSelectionParameters = selectionBuilder.build()

        subtitleTrackListener?.let {

            it(track)
        }
    }


    fun setPlaybackParameters(speed: Float) {

        val playbackParameters = PlaybackParameters(speed)
        exoPlayer.playbackParameters = playbackParameters
    }


    fun setVideoSurface(surface: Surface?) {

        exoPlayer.setVideoSurface(surface)
    }


    fun seekToPosition(pos: Long) {

        exoPlayer.seekTo(pos)
    }


    @OptIn(UnstableApi::class)
    fun initUrl(streamUrl: String, movieId: String) {

        val mediaItem =
            DownloadManagerUtil.getOfflineMediaItem(context = context, downloadId = movieId)

        if (mediaItem != null) {

            exoPlayer.setMediaItem(mediaItem)
        } else {

            exoPlayer.setMediaItem(Builder().setUri(streamUrl).build())
        }

//
//        if (mediaItem != null) {
//
//            exoPlayer = ExoPlayer.Builder(context)
//                .setMediaSourceFactory(
//                    DefaultMediaSourceFactory(DownloadManagerUtil.buildDataSourceFactory(context))
//                ).build()
//
//            exoPlayer.setMediaItem(mediaItem)
//
//
//        } else {
//            exoPlayer
//            exoPlayer.setMediaItem(Builder().setUri(streamUrl).build())
//        }

    }


    fun pause() {

        exoPlayer.pause()
    }


    fun play() {

        exoPlayer.play()
    }


    fun start(positionInMs: Long?) {

        with(exoPlayer) {

            prepare()
            play()
            positionInMs?.let {

                seekTo(it)
            }
        }
    }


    fun stop() {

        exoPlayer.stop()
    }


}
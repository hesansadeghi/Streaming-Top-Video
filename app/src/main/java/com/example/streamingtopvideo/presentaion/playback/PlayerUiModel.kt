package com.example.streamingtopvideo.presentaion.playback

import androidx.media3.common.text.Cue

data class PlayerUiModel(
    val titleMovie: String = "",
    val videoAspectRatio: Float = 16.0f / 9.0f,
    val playerControlsVisible: Boolean = true,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val timelineUiModel: TimelineUiModel? = null,
    val trackSelectionUiModel: TrackSelectionUiModel? = null,
    val videoSpeedUiModel: VideoSpeedUiModel? = VideoSpeedUiModel(),
    val isTrackSelectorVisible: Boolean = false,
    val isVideoSpeedVisible: Boolean = false,
    val currentSubtitles: List<Cue> = emptyList(),
    val popBackStack: Boolean = false,
)


enum class PlaybackState {
    IDLE, PLAYING, PAUSE, BUFFERING, COMPLETED, ERROR
}


data class TimelineUiModel(
    val durationInMs: Long,
    val currentPositionInMs: Long,
    val bufferedPositionInMs: Long,
)


data class TrackSelectionUiModel(
    val selectedVideoTrack: VideoTrack,
    val videoTracks: List<VideoTrack>,
    val selectedAudioTrack: AudioTrack,
    val audioTracks: List<AudioTrack>,
    val selectedSubtitleTrack: SubtitleTrack,
    val subtitleTracks: List<SubtitleTrack>
)


data class VideoSpeedUiModel(
    val selectedSpeed: Float = 1f,
    val speedValues: List<Float> = listOf(2.0f, 1.5f, 1.25f, 1.0f, 0.75f,  0.5f, 0.25f)
)


data class SubtitleTrack(
    val language: String
) {

    val displayName: String
        get() {
            return when (this) {
                AUTO -> "Auto"
                NONE -> "None"
                else -> language
            }
        }

    companion object {
        val AUTO = SubtitleTrack("Auto")
        val NONE = SubtitleTrack("None")
    }

}


data class AudioTrack(
    val language: String
) {

    val displayName: String
        get() {
            return when (this) {
                AUTO -> "Auto"
                NONE -> "None"
                else -> language
            }
        }

    companion object {
        val AUTO = AudioTrack("Auto")
        val NONE = AudioTrack("None")
    }

}

data class VideoTrack(
    val width: Int,
    val height: Int
) {

    val displayName: String
        get() {
            return when (this) {

                AUTO -> "Auto"
                else -> "$width $height"
            }

        }

    companion object {
        val AUTO = VideoTrack(0, 0)
    }

}

fun PlaybackState.isReady() = (this == PlaybackState.PLAYING || this == PlaybackState.PAUSE)

package com.example.streamingtopvideo.presentaion.playback

import android.view.Surface

sealed interface PlayerUiEvent

data class Init(val streamUrl: String,val movieId: String, val movieTitle: String) : PlayerUiEvent
data object Pause : PlayerUiEvent
data class Start(val positionInMs: Long? = null) : PlayerUiEvent
data object Stop : PlayerUiEvent
data object Resume : PlayerUiEvent
data class Rewind(val amountInMs: Long) : PlayerUiEvent
data class FastForward(val amountInMs: Long) : PlayerUiEvent
data class Seek(val targetInMs: Long) : PlayerUiEvent
data class AttachSurface(val surface: Surface) : PlayerUiEvent
data object DetachSurface : PlayerUiEvent
data object BackStack : PlayerUiEvent
data class SetVideoTrack(val track: VideoTrack) : PlayerUiEvent
data class SetAudioTrack(val track: AudioTrack) : PlayerUiEvent
data class SetSubtitleTrack(val track: SubtitleTrack) : PlayerUiEvent

data class SetPlaybackParameters(val speed: Float) : PlayerUiEvent

package com.example.feature.video

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.example.core.domain.respository.VideoRepositoryImpl
import com.example.core.model.MediaStoreResult
import com.example.core.model.VideoModel
import com.example.feature.video.model.VideoPlayerOverlayState
import com.example.video_media3.VideoMedia3Controller
import com.example.video_media3.util.VideoThumbnailUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class VideoViewModel(
    private var videoSource: VideoRepositoryImpl,
    private var videoThumbnailUtil: VideoThumbnailUtil,
    private var videoMedia3Controller: VideoMedia3Controller,
) : ViewModel() {
    private var _uiState = MutableStateFlow(VideoUiState())
    var uiState = _uiState
        .onStart {
            getVideo()
            observeCurrentPlayerPosition()
            observeVideoPlayerState()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), VideoUiState())

    private var _previewSliderBitmap = Channel<ImageBitmap?>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    var previewSliderBitmap = _previewSliderBitmap.receiveAsFlow()

    private var getThumbnailJob: Job? = null

    fun getSliderPreviewThumbnail(position: Long) {
        if (getThumbnailJob?.isActive == true) getThumbnailJob?.cancel()

        getThumbnailJob = viewModelScope.launch {
            val videoUri = _uiState.value.videoPlayerState.currentMediaInfo.videoUri.toUri()
            val thumbnailBitmap = videoThumbnailUtil.getVideoThumbNail(videoUri, position)
            _previewSliderBitmap.send(thumbnailBitmap?.asImageBitmap())
        }
    }

    fun pausePlayer() = viewModelScope.launch { videoMedia3Controller.pausePlayer() }

    val getExoPlayer: ExoPlayer = videoMedia3Controller.getPlayer

    fun startPlay(index: Int, videoList: List<VideoModel>) = viewModelScope.launch {
        videoMedia3Controller.startPlay(index, videoList)
    }

    fun startPlayFromUri(uri: Uri) = viewModelScope.launch {
        videoMedia3Controller.startPlayFromUri(uri)
    }

    private fun observeCurrentPlayerPosition() {
        viewModelScope.launch {
            while (currentCoroutineContext().isActive) {
                delay(50L)
                if (_uiState.value.videoPlayerState.isPlaying) {
                    _uiState.update { it.copy(currentPlayerPosition = videoMedia3Controller.getPlayer.currentPosition) }
                }
            }
        }
    }

    private fun observeVideoPlayerState() = viewModelScope.launch {
        videoMedia3Controller.registerMedia3Listener()
        videoMedia3Controller.videoPlayerStateStateFlow.collect { videoPlayerState ->
            _uiState.update { it.copy(videoPlayerState = videoPlayerState) }
        }
    }

    fun resumePlayer() = viewModelScope.launch { videoMedia3Controller.resumePlayer() }

    fun seekToNext() = viewModelScope.launch { videoMedia3Controller.seekToNextMediaItem() }

    fun fastForward(position: Long, currentPosition: Long) = viewModelScope.launch {
        videoMedia3Controller.fastForward(position, currentPosition)
    }

    fun fastRewind(position: Long, currentPosition: Long) = viewModelScope.launch {
        videoMedia3Controller.fastRewind(position, currentPosition)
    }

    fun updateMiddleVideoPlayerInfo(middleVideoPlayerInfo: VideoPlayerOverlayState) = viewModelScope.launch {
        _uiState.update { it.copy(videoPlayerOverlayState = middleVideoPlayerInfo) }
        delay(4_000L)
        _uiState.update { it.copy(videoPlayerOverlayState = VideoPlayerOverlayState.Initial) }
    }

    fun seekToPrevious() = viewModelScope.launch { videoMedia3Controller.seekToPreviousMediaItem() }

    fun seekToPosition(position: Long) = viewModelScope.launch {
        videoMedia3Controller.seekTo(position)
        _uiState.update { it.copy(currentPlayerPosition = position) }
    }

    fun stopPlayer() = viewModelScope.launch {
        videoMedia3Controller.stop()
    }

    fun setVideoPlayerOverlayControllerVisibility(boolean: Boolean) {
        _uiState.update { it.copy(isVideoPlayerOverlayControllerVisible = boolean) }
    }

    fun getVideo() = viewModelScope.launch {
        videoSource.getVideos().collect { result ->
            when (result) {
                MediaStoreResult.Empty ->
                    _uiState.update { it.copy(loading = false, videoList = emptyList()) }

                MediaStoreResult.Loading ->
                    _uiState.update { it.copy(loading = true) }

                is MediaStoreResult.Result<VideoModel> ->
                    _uiState.update { it.copy(loading = false, videoList = result.dataList) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getThumbnailJob?.cancel()
        videoMedia3Controller.unRegisterMedia3Listener()
    }
}

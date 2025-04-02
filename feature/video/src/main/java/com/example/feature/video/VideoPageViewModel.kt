package com.example.feature.video

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.example.core.domain.api.VideoRepositoryImpl
import com.example.core.model.MediaStoreResult
import com.example.core.model.VideoModel
import com.example.feature.video.model.MiddleVideoPlayerIndicator
import com.example.video_media3.VideoMedia3Controller
import com.example.video_media3.model.VideoPlayerState
import com.example.video_media3.util.VideoThumbnailUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class VideoPageViewModel(
  private var videoSource: VideoRepositoryImpl,
  private var videoThumbnailUtil: VideoThumbnailUtil,
  private var videoMedia3Controller: VideoMedia3Controller,
) : ViewModel() {

  private var _uiState = MutableStateFlow<MediaStoreResult<VideoModel>>(MediaStoreResult.Loading)
  var uiState = _uiState
    .onStart { getVideo() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), MediaStoreResult.Loading)

  private var _previewSliderBitmap = Channel<ImageBitmap?>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
  var previewSliderBitmap = _previewSliderBitmap.receiveAsFlow()

  private var _middleVideoPlayerInfo = MutableSharedFlow<MiddleVideoPlayerIndicator>()
  var middleVideoPlayerInfo = _middleVideoPlayerInfo.asSharedFlow()

  private var getThumbnailJob: Job? = null
  private var currentPlayerPositionJob: Job? = null

  val currentPlayerState = videoMedia3Controller.videoPlayerStateStateFlow
    .onStart { videoMedia3Controller.registerMedia3Listener() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), VideoPlayerState.Empty)

  private var _currentPlayerPosition = MutableStateFlow(0L)
  var currentPlayerPosition = _currentPlayerPosition
    .onStart {
      observeCurrentPlayerPosition()
    }.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      0,
    )

  fun getSliderPreviewThumbnail(position: Long) {
    if (getThumbnailJob?.isActive == true) getThumbnailJob?.cancel()

    getThumbnailJob = viewModelScope.launch {
      val videoUri = currentPlayerState.value.currentMediaInfo.videoUri.toUri()
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
    currentPlayerPositionJob = viewModelScope.launch {
      while (currentCoroutineContext().isActive && currentPlayerPositionJob?.isActive == true) {
        delay(50L)
        if (currentPlayerState.value.isPlaying) {
          _currentPlayerPosition.value = videoMedia3Controller.getPlayer.currentPosition
        }
      }
    }
    currentPlayerPositionJob?.start()
  }

  fun resumePlayer() = viewModelScope.launch { videoMedia3Controller.resumePlayer() }

  fun seekToNext() = viewModelScope.launch { videoMedia3Controller.seekToNextMediaItem() }

  fun fastForward(position: Long, currentPosition: Long) = viewModelScope.launch {
    videoMedia3Controller.fastForward(position, currentPosition)
  }

  fun fastRewind(position: Long, currentPosition: Long) = viewModelScope.launch {
    videoMedia3Controller.fastRewind(position, currentPosition)
  }

  fun updateMiddleVideoPlayerInfo(middleVideoPlayerInfo: MiddleVideoPlayerIndicator) = viewModelScope.launch {
    _middleVideoPlayerInfo.emit(middleVideoPlayerInfo)
    delay(4_000L)
    _middleVideoPlayerInfo.emit(MiddleVideoPlayerIndicator.Initial)
  }

  fun seekToPrevious() = viewModelScope.launch { videoMedia3Controller.seekToPreviousMediaItem() }

  fun seekToPosition(position: Long) = viewModelScope.launch {
    videoMedia3Controller.seekTo(position)
    _currentPlayerPosition.update { position }
  }

  fun stopPlayer() = viewModelScope.launch {
    videoMedia3Controller.stop()
  }

  fun getVideo() = viewModelScope.launch {
    videoSource.getVideos().collect {
      _uiState.value = it
    }
  }

  override fun onCleared() {
    super.onCleared()
    currentPlayerPositionJob?.cancel()
    getThumbnailJob?.cancel()
    videoMedia3Controller.unRegisterMedia3Listener()
  }

}
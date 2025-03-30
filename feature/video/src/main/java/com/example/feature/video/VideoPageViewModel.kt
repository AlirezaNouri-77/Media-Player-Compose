package com.example.feature.video

import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.ui.AspectRatioFrameLayout
import com.example.core.data.mapper.toActiveVideoInfo
import com.example.core.data.mapper.toMediaItem
import com.example.core.model.ActiveVideoInfo
import com.example.core.data.util.MediaThumbnailUtil
import com.example.core.data.util.VideoMediaMetaData
import com.example.core.domain.api.VideoSourceImpl
import com.example.core.model.MediaStoreResult
import com.example.core.model.VideoModel
import com.example.feature.video.model.MiddleVideoPlayerIndicator
import com.example.feature.video.model.VideoPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class VideoPageViewModel(
  private var videoSource: VideoSourceImpl,
  private var videoMediaMetaData: VideoMediaMetaData,
  private var mediaThumbnailUtil: MediaThumbnailUtil,
  private var videoExoPlayer: ExoPlayer,
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

  private var playerListener: Player.Listener

  private var _currentState = MutableStateFlow(VideoPlayerState.Empty)
  val playerStateModel: StateFlow<VideoPlayerState> = _currentState.asStateFlow()

  private var _currentPlayerPosition = MutableStateFlow(0L)
  var currentPlayerPosition = _currentPlayerPosition
    .onStart {
      observeCurrentPlayerPosition()
    }.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5_000L),
      0,
    )

  init {

    playerListener = object : Player.Listener {

      override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        _currentState.update { it.copy(isPlaying = playWhenReady) }
      }

      override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
          Player.STATE_BUFFERING -> _currentState.update { it.copy(isBuffering = true) }
          Player.STATE_READY -> _currentState.update { it.copy(isBuffering = false) }
          else -> {}
        }
      }

      override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        _currentState.update {
          it.copy(currentMediaInfo = mediaItem?.toActiveVideoInfo() ?: ActiveVideoInfo.Empty)
        }
      }

    }
    videoExoPlayer.addListener(playerListener)
  }

  fun getSliderPreviewThumbnail(position: Long) {
    if (getThumbnailJob?.isActive == true) getThumbnailJob?.cancel()

    getThumbnailJob = viewModelScope.launch {
      val videoUri = _currentState.value.currentMediaInfo.videoUri.toUri()
      val thumbnailBitmap = mediaThumbnailUtil.getVideoThumbNail(videoUri, position)
      _previewSliderBitmap.send(thumbnailBitmap?.asImageBitmap())
    }
  }

  fun pausePlayer() = viewModelScope.launch { videoExoPlayer.pause() }

  val getExoPlayer: ExoPlayer = videoExoPlayer

  fun startPlay(index: Int, videoList: List<VideoModel>) {
    viewModelScope.launch {
      videoExoPlayer.apply {
        this.setMediaItems(videoList.map(VideoModel::toMediaItem), index, 0L)
        this.playWhenReady = true
        this.prepare()
        this.play()
      }
    }
  }

  fun startPlayFromUri(uri: Uri) {
    viewModelScope.launch {
      val metaData = videoMediaMetaData.get(uri)
      metaData?.let {
        videoExoPlayer.apply {
          this.addMediaItem(metaData.toMediaItem())
          this.playWhenReady = true
          this.prepare()
          this.play()
        }
      }
    }
  }

  private fun observeCurrentPlayerPosition() {
    currentPlayerPositionJob = viewModelScope.launch {
      while (currentCoroutineContext().isActive && currentPlayerPositionJob?.isActive == true) {
        delay(50L)
        if (videoExoPlayer.isPlaying) {
          _currentPlayerPosition.value = videoExoPlayer.currentPosition
        }
      }
    }
    currentPlayerPositionJob?.start()
  }

  fun resumePlayer() = viewModelScope.launch { videoExoPlayer.play() }

  fun seekToNext() = viewModelScope.launch { videoExoPlayer.seekToNextMediaItem() }

  fun fastForward(position: Long, currentPosition: Long) = viewModelScope.launch {
    videoExoPlayer.seekTo(currentPosition + position)
  }

  fun updateMiddleVideoPlayerInfo(middleVideoPlayerInfo: MiddleVideoPlayerIndicator) = viewModelScope.launch {
    _middleVideoPlayerInfo.emit(middleVideoPlayerInfo)
    delay(4_000L)
    _middleVideoPlayerInfo.emit(MiddleVideoPlayerIndicator.Initial)
  }

  fun fastRewind(position: Long, currentPosition: Long) = viewModelScope.launch {
    videoExoPlayer.seekTo(currentPosition - position)
  }

  fun seekToPrevious() = viewModelScope.launch { videoExoPlayer.seekToPreviousMediaItem() }

  fun seekToPosition(position: Long) = viewModelScope.launch {
    videoExoPlayer.seekTo(position)
    _currentPlayerPosition.update { position }
  }

  private fun releasePlayer() = viewModelScope.launch {
    videoExoPlayer.pause()
    videoExoPlayer.clearMediaItems()
    videoExoPlayer.removeListener(playerListener)
    videoExoPlayer.release()
  }

  fun stopPlayer() = viewModelScope.launch {
    videoExoPlayer.pause()
    videoExoPlayer.clearMediaItems()
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
    releasePlayer()
  }

}
package com.example.mediaplayerjetpackcompose.presentation.screen.video

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.mediaplayerjetpackcompose.data.mapper.toMediaItem
import com.example.mediaplayerjetpackcompose.data.util.GetMediaMetaData
import com.example.mediaplayerjetpackcompose.data.util.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.repository.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class VideoPageViewModel(
  private var videoMediaStoreRepository: MediaStoreRepositoryImpl<VideoItemModel>,
  private var getMediaMetaData: GetMediaMetaData,
  private var mediaThumbnailUtil: MediaThumbnailUtil,
  private var exoPlayer: ExoPlayer,
) : ViewModel() {

  var playerResizeMode by mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT)

  private var _uiState = MutableStateFlow<MediaStoreResult<VideoItemModel>>(MediaStoreResult.Loading)
  var uiState = _uiState
    .onStart { getVideo() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), MediaStoreResult.Loading)

  private var _previewSliderBitmap = Channel<ImageBitmap?>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
  var previewSliderBitmap = _previewSliderBitmap.receiveAsFlow()

  private var _currentPlayerPosition = MutableStateFlow(0L)
  var currentPlayerPosition = _currentPlayerPosition.asStateFlow()

  private var getThumbnailJob: Job? = null

  private var _currentState = MutableStateFlow(CurrentMediaState.Empty)
  val currentMediaState: StateFlow<CurrentMediaState> = _currentState.asStateFlow()

  init {
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
    viewModelScope.launch {
      while (viewModelScope.isActive) {
        delay(50L)
        if (exoPlayer.isPlaying) {
          _currentPlayerPosition.value = exoPlayer.currentPosition
        }
      }
    }
    viewModelScope.launch(Dispatchers.IO) {
      exoPlayer.addListener(
        object : Player.Listener {
          override fun onIsPlayingChanged(isPlaying: Boolean) {
            viewModelScope.launch {
              _currentState.update { it.copy(isPlaying = isPlaying) }
            }
          }

          override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
              Player.STATE_BUFFERING -> _currentState.update { it.copy(isBuffering = true) }
              Player.STATE_READY -> _currentState.update { it.copy(isBuffering = false) }
              else -> {}
            }
          }

          override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            viewModelScope.launch {
              _currentState.update { it.copy(uri = mediaItem?.localConfiguration?.uri ?: Uri.EMPTY) }
            }
          }

          override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            viewModelScope.launch {
              _currentState.update { it.copy(metaData = mediaMetadata) }
            }
          }
        },
      )
    }
  }

  fun getSliderPreviewThumbnail(position: Long) {
    if (_currentState.value.uri == Uri.EMPTY) return
    getThumbnailJob?.cancel()
    getThumbnailJob = viewModelScope.launch {
      val thumbnailBitmap = mediaThumbnailUtil.getVideoThumbNail(_currentState.value.uri, position)
      _previewSliderBitmap.send(thumbnailBitmap)
    }
  }

  fun pausePlayer() = viewModelScope.launch { exoPlayer.pause() }

  fun getExoPlayer(): ExoPlayer = exoPlayer

  fun startPlay(index: Int, videoList: List<VideoItemModel>) {
    viewModelScope.launch {
      exoPlayer.apply {
        this.setMediaItems(videoList.map(VideoItemModel::toMediaItem), index, 0L)
        this.playWhenReady = true
        this.prepare()
        this.play()
      }
    }
  }

  fun startPlayFromUri(uri: Uri) {
    viewModelScope.launch {
      val metaData = getMediaMetaData.get(uri)
      metaData?.let {
        exoPlayer.apply {
          this.addMediaItem(metaData.toMediaItem())
          this.playWhenReady = true
          this.prepare()
          this.play()
        }
      }
    }
  }

  fun resumePlayer() = viewModelScope.launch { exoPlayer.play() }

  fun seekToNext() = viewModelScope.launch { exoPlayer.seekToNextMediaItem() }

  fun fastForward(position: Long, currentPosition: Long) = viewModelScope.launch {
    exoPlayer.seekTo(currentPosition + position)
  }

  fun fastRewind(position: Long, currentPosition: Long) = viewModelScope.launch {
    exoPlayer.seekTo(currentPosition - position)
  }

  fun seekToPrevious() = exoPlayer.seekToPreviousMediaItem()

  fun seekToPosition(position: Long) = viewModelScope.launch {
    exoPlayer.seekTo(position)
    _currentPlayerPosition.value = position
  }

  fun releasePlayer() = viewModelScope.launch {
    exoPlayer.pause()
    exoPlayer.clearMediaItems()
    exoPlayer.release()
  }

  fun stopPlayer() = viewModelScope.launch {
    exoPlayer.pause()
    exoPlayer.clearMediaItems()
  }

  private fun getVideo() {
    viewModelScope.launch {
      videoMediaStoreRepository.getMedia().collect {
        _uiState.value = it
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    releasePlayer()
  }

}



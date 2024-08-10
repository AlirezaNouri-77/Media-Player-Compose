package com.example.mediaplayerjetpackcompose.presentation.screen.video

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.mediaplayerjetpackcompose.data.service.MediaCurrentState
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoModel
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.toMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(UnstableApi::class)
class VideoPageViewModel(
  private var videoMediaStoreRepository: MediaStoreRepositoryImpl<VideoModel>,
  private var exoPlayer: ExoPlayer,
) : ViewModel() {

  var isLoading by mutableStateOf(true)
  var deviceOrientation by mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT)
  var mediaStoreDataList = mutableStateListOf<VideoModel>()
    private set

  init {
    getVideo()
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
    viewModelScope.launch(Dispatchers.Main) {
      exoPlayer.addListener(
        object : Player.Listener {
          override fun onIsPlayingChanged(isPlaying: Boolean) {
            _currentState.update { it.copy(isPlaying = isPlaying) }
          }

          override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            _currentState.update { it.copy(metaData = mediaMetadata) }
          }
        },
      )
    }
  }

  private var _currentState = MutableStateFlow(
    MediaCurrentState(
      isPlaying = false,
      mediaId = "",
      metaData = MediaMetadata.EMPTY,
      uri = Uri.EMPTY,
      isBuffering = false,
    )
  )
  val currentState: StateFlow<MediaCurrentState> = _currentState.asStateFlow()
  val currentMediaPosition = flow {
    while (currentCoroutineContext().isActive) {
      emit(exoPlayer.currentPosition)
      delay(1000L)
    }
  }

  fun pausePlayer() = exoPlayer.pause()

  fun provideExoPlayer(): ExoPlayer = exoPlayer

  fun startPlay(index: Int, videoList: List<VideoModel>) {
    exoPlayer.apply {
      this.setMediaItems(videoList.map(VideoModel::toMediaItem), index, 0L)
      this.playWhenReady = true
      this.prepare()
      this.play()
    }
  }

  fun startPlayFromUri(uri: Uri) {
    exoPlayer.apply {
      this.addMediaItem(MediaItem.fromUri(uri))
      this.playWhenReady = true
      this.prepare()
      this.play()
    }
  }

  fun resumePlayer() = exoPlayer.play()

  fun seekToNext() = exoPlayer.seekToNextMediaItem()

  fun fastForward(position: Long, currentPosition: Long) {
    exoPlayer.seekTo(currentPosition + position)
  }

  fun fastRewind(position: Long, currentPosition: Long) {
    exoPlayer.seekTo(currentPosition - position)
  }

  fun seekToPrevious() = exoPlayer.seekToPreviousMediaItem()

  fun seekToPosition(position: Long) {
    exoPlayer.seekTo(position)
  }

  fun releasePlayer() = viewModelScope.launch(Dispatchers.Main) {
    exoPlayer.pause()
    exoPlayer.clearMediaItems()
    exoPlayer.release()
  }

  fun stopPlayer() = viewModelScope.launch(Dispatchers.Main) {
    exoPlayer.pause()
    exoPlayer.clearMediaItems()
  }

  private fun getVideo() {
    viewModelScope.launch {
      videoMediaStoreRepository.getMedia().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialValue = MediaStoreResult.Initial,
      ).collect {
        withContext(Dispatchers.Main) {
          when (it) {
            MediaStoreResult.Loading -> {
              isLoading = true
            }

            is MediaStoreResult.Result -> {
              mediaStoreDataList.addAll(it.result)
              isLoading = false
            }

            else -> {}
          }
        }
      }
    }
  }


  override fun onCleared() {
    super.onCleared()
    releasePlayer()
  }

}



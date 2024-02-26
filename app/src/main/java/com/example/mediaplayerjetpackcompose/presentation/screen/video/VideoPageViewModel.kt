package com.example.mediaplayerjetpackcompose.presentation.screen.video

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.mediaplayerjetpackcompose.data.application.ApplicationClass
import com.example.mediaplayerjetpackcompose.data.MediaCurrentState
import com.example.mediaplayerjetpackcompose.data.GetMediaArt
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreResult
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel
import com.example.mediaplayerjetpackcompose.domain.model.toMediaItem
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
  private var applicationClass: ApplicationClass,
  private var videoMediaStoreRepository: VideoMediaStoreRepository,
  private var getMediaArt: GetMediaArt,
) : AndroidViewModel(applicationClass) {

  var exoPlayer: ExoPlayer
  var mediaStoreDataList = mutableStateListOf<VideoMediaModel>()
    private set
  var videoThumbnailBitmap = mutableStateListOf<videoThumbNailsModel>()

  init {
    getVideo()
    exoPlayer = ExoPlayer.Builder(applicationClass.applicationContext).build()
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

  var deviceOrientation by mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT)

  private var _currentState = MutableStateFlow(
    MediaCurrentState(
      isPlaying = false,
      mediaId = "",
      metaData = MediaMetadata.EMPTY,
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

  suspend fun getVideoThumbNail(uri: Uri): Bitmap? {
    return getMediaArt.getVideoThumbNail(uri)
  }

  fun startPlay(index: Int, videoList: List<VideoMediaModel>) {
    exoPlayer.apply {
      this.setMediaItems(videoList.map(VideoMediaModel::toMediaItem), index, 0L)
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
      ).collect { it ->
        withContext(Dispatchers.Main) {
          when (it) {
            MediaStoreResult.Loading -> {

            }

            is MediaStoreResult.Result -> {
              mediaStoreDataList.addAll(it.result)
            }

            else -> {}
          }
        }
      }
    }
  }

  companion object {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
      initializer {
        val application = checkNotNull((this[APPLICATION_KEY])) as ApplicationClass
        val savedStateHandle = createSavedStateHandle()
        VideoPageViewModel(
          applicationClass = application,
          videoMediaStoreRepository = application.videoMediaStoreRepository,
          getMediaArt = application.getMediaArt,
        )
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    releasePlayer()
  }

}

data class videoThumbNailsModel(var bitmap: Bitmap, var musicId: Long)

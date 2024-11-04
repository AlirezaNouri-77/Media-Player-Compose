package com.example.mediaplayerjetpackcompose.data.service

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive

class MusicServiceConnection(
  private var context: Context,
) {

  private var _currentMediaState = MutableStateFlow(CurrentMediaState.Empty)
  var currentMediaState: StateFlow<CurrentMediaState> = _currentMediaState.asStateFlow()

  private var factory: ListenableFuture<MediaController>? = null

  var mediaController: MediaController? = null

  init {
    initialExoPlayer()
  }

  var musicPosition: Flow<Long> = flow {
    while (currentCoroutineContext().isActive) {
      delay(50L)
      if (mediaController?.isPlaying == true) {
        val position = mediaController?.currentPosition ?: 0L
        emit(position)
      }
    }
  }

  private fun initialExoPlayer() {
    if (factory == null) {
      factory = MediaController.Builder(
        context, SessionToken(
          context, ComponentName(context, MusicPlayerService::class.java)
        )
      ).buildAsync().also { mediaControllerFuture ->
        mediaControllerFuture.addListener(
          {
            mediaController = factory?.get().also { it?.addListener(exoPlayerListener) }
          },
          MoreExecutors.directExecutor(),
        )
      }
    }
  }

  private val exoPlayerListener = object : Player.Listener  {

    override fun onIsPlayingChanged(isPlaying: Boolean) {
      _currentMediaState.update { it.copy(isPlaying = isPlaying) }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
      _currentMediaState.update { it.copy(metaData = mediaMetadata) }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
      _currentMediaState.update {
        it.copy(
          mediaId = mediaItem?.mediaId ?: "",
          uri = mediaItem?.localConfiguration?.uri ?: Uri.EMPTY
        )
      }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
      when (playbackState) {
        Player.STATE_BUFFERING -> _currentMediaState.update { it.copy(isBuffering = true) }
        Player.STATE_READY -> _currentMediaState.update { it.copy(isBuffering = false) }
        else -> {}
      }
    }
  }

}

package com.example.mediaplayerjetpackcompose

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mediaplayerjetpackcompose.data.MusicPlayerService
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import com.example.mediaplayerjetpackcompose.domain.model.toMediaItem
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive

class PlayBackHandler(private var context: Context) {

  init {
    initialExoPlayer()
  }

  private var factory: ListenableFuture<MediaController>? = null
  var mediaController by mutableStateOf<MediaController?>(null)
    private set
  var currentMusicPosition = flow {
    while (currentCoroutineContext().isActive) {
      val position = mediaController?.currentPosition ?: 0L
      emit(position)
      delay(1000L)
    }
  }

  private var _musicState =
    MutableStateFlow(MusicState(isPlaying = false, metadata = MediaMetadata.EMPTY, mediaId = ""))
  var musicState: StateFlow<MusicState> = _musicState.asStateFlow()

  private fun initialExoPlayer() {
    if (factory == null) {
      factory = MediaController.Builder(
        context, SessionToken(
          context, ComponentName(context, MusicPlayerService::class.java)
        )
      ).buildAsync().also {
        it.addListener(
          {
            mediaController = factory?.get().also { it?.addListener(exoPlayerListener) }
          },
          MoreExecutors.directExecutor(),
        )
      }
    }
  }

  fun moveToNext() = mediaController?.seekToNext()
  fun moveToPrevious() = mediaController?.seekToPrevious()
  fun resumeMusic() = mediaController?.play()
  fun pauseMusic() = mediaController?.pause()
  fun seekToPosition(position: Long) = mediaController?.seekTo(position)

  fun playMusic(index: Int, musicList: List<MusicMediaModel>) = mediaController?.let {
    it.setMediaItems(musicList.map(MusicMediaModel::toMediaItem), index, 0L)
    it.playWhenReady
    it.prepare()
    it.play()
  }

  fun updateMediaList(index: Int, musicList: List<MusicMediaModel>, startPosition: Long) =
    mediaController?.setMediaItems(
      musicList.map(MusicMediaModel::toMediaItem),
      index,
      startPosition
    )

  private val exoPlayerListener = object : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
      _musicState.update { it.copy(isPlaying = isPlaying) }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
      _musicState.update { it.copy(metadata = mediaMetadata) }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
      _musicState.update { it.copy(mediaId = mediaItem?.mediaId ?: "") }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
      if (playbackState == Player.STATE_ENDED) {
        mediaController?.seekToNextMediaItem()
      }
    }
  }

}

data class MusicState(var isPlaying: Boolean, val metadata: MediaMetadata, val mediaId: String)
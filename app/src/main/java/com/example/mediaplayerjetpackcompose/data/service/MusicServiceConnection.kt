package com.example.mediaplayerjetpackcompose.data.service

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mediaplayerjetpackcompose.data.mapper.toMediaItem
import com.example.mediaplayerjetpackcompose.data.util.Constant
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.share.CurrentMediaState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class MusicServiceConnection(
  private var context: Context,
) {

  private var _currentMediaState =
    MutableStateFlow(
      CurrentMediaState(
        isPlaying = false,
        metaData = MediaMetadata.EMPTY,
        mediaId = "",
        uri = Uri.EMPTY,
        isBuffering = false,
      )
    )
  var currentMediaState: StateFlow<CurrentMediaState> = _currentMediaState.asStateFlow()

  val currentRepeatMode = mutableIntStateOf(0)

  private var factory: ListenableFuture<MediaController>? = null
  private var mediaController by mutableStateOf<MediaController?>(null)

  init {
    initialExoPlayer()
  }

  fun musicPosition(): Flow<Long> {
    return flow {
      while (true) {
        delay(50L)
        if (mediaController?.isPlaying == true) {
          val position = mediaController?.currentPosition ?: 0L
          emit(position)
        }
      }
    }
  }

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

  fun moveToMediaIndex(index: Int) = mediaController?.seekTo(index, 0L)

  fun moveToPrevious() = mediaController?.seekToPrevious()

  fun moveToPreviousMediaItem() = mediaController?.seekToPreviousMediaItem()

  fun resumeMusic() = mediaController?.play()

  fun pauseMusic() = mediaController?.pause()

  fun seekToPosition(position: Long) = mediaController?.seekTo(position)

  fun setPlayerRepeatMode(repeatMode: Int) {
    currentRepeatMode.intValue = repeatMode
    mediaController?.let {
      it.repeatMode = Constant.RepeatModes[currentRepeatMode.intValue]
    }
  }

  fun hasNextItem(): Boolean = mediaController?.hasNextMediaItem() ?: false

  fun hasPreviewItem(): Boolean = mediaController?.hasPreviousMediaItem() ?: false

  fun playMusic(index: Int, musicList: List<MusicModel>) = mediaController?.let {
    it.setMediaItems(musicList.map(MusicModel::toMediaItem), index, 0L)
    it.playWhenReady
    it.prepare()
    it.play()
  }

  fun updateMediaList(index: Int, musicList: List<MusicModel>, startPosition: Long) =
    mediaController?.setMediaItems(
      musicList.map(MusicModel::toMediaItem),
      index,
      startPosition
    )

  private val exoPlayerListener = object : Player.Listener {
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

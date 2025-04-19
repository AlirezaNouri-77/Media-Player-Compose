package com.example.core.music_media3

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.core.model.ActiveMusicInfo
import com.example.core.model.MusicModel
import com.example.core.music_media3.mapper.toActiveMusicInfo
import com.example.core.music_media3.mapper.toMediaItem
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicServiceConnection(
  private var context: Context,
) {

  private var factory: ListenableFuture<MediaController>? = null
  private var mediaController: MediaController? = null

  private var _playerState = MutableStateFlow(PlayerStateModel.Empty)
  var playerState: StateFlow<PlayerStateModel> = _playerState.asStateFlow()

  private var _currentArtworkPagerIndex = MutableStateFlow(0)
  var currentArtworkPagerIndex: StateFlow<Int> = _currentArtworkPagerIndex.asStateFlow()

  private var _artworkPagerList = MutableStateFlow(listOf<MusicModel>())
  var artworkPagerList = _artworkPagerList.asStateFlow()

  init {
    initialExoPlayer()
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

  fun currentPlayerPosition() = mediaController?.currentPosition ?: 0L

  fun pauseMusic() = mediaController?.pause()

  fun resumeMusic() = mediaController?.play()

  fun seekToPosition(position: Long) {
    mediaController?.seekTo(position)
  }

  fun moveToMediaIndex(index: Int) {
    mediaController?.apply {
      seekTo(index, 0L)
      prepare()
      play()
    }
  }

  fun playSongs(index: Int, musicList: List<MusicModel>) {
    _currentArtworkPagerIndex.update { index }
    _artworkPagerList.update { musicList.toList() }
    mediaController?.run {
      setMediaItems(musicList.map(MusicModel::toMediaItem), index, 0L)
      prepare()
      play()
    }
  }

  fun setRepeatMode(repeatMode: Int) {
    mediaController?.repeatMode = repeatMode
  }

  fun setCurrentArtworkPagerIndex(index: Int) = _currentArtworkPagerIndex.update { index }

  fun moveToNext() {
    val hasNextItem = mediaController?.hasNextMediaItem() == true
    if (!hasNextItem) return
    mediaController?.apply {
      seekToNext()
      prepare()
      play()
    }
    _currentArtworkPagerIndex.update { it + 1 }
  }

  fun moveToPrevious(seekToStart: Boolean = false, currentMusicPosition: Long) {
    val hasPreviewItem = mediaController?.hasNextMediaItem() == true
    if (!hasPreviewItem) return

    if (currentMusicPosition <= 15_000 || seekToStart) {
      mediaController?.apply {
        seekToPreviousMediaItem()
        prepare()
        play()
      }
      _currentArtworkPagerIndex.update { it - 1 }
    } else mediaController?.apply {
      seekToPrevious()
      prepare()
      play()
    }
  }

  private val exoPlayerListener = object : Player.Listener {

    override fun onRepeatModeChanged(repeatMode: Int) {
      _playerState.update { it.copy(repeatMode = repeatMode) }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
      _playerState.update { it.copy(isPlaying = playWhenReady) }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
      _playerState.update {
        it.copy(
          currentMediaInfo = mediaItem?.toActiveMusicInfo() ?: ActiveMusicInfo.Empty
        )
      }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
      when (playbackState) {
        Player.STATE_BUFFERING -> _playerState.update { it.copy(isBuffering = true) }
        Player.STATE_READY -> _playerState.update { it.copy(isBuffering = false) }
        else -> {}
      }
    }
  }

}

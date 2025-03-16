package com.example.core.music_media3

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.core.data.mapper.toActiveMusicInfo
import com.example.core.data.mapper.toMediaItem
import com.example.core.model.ActiveMusicInfo
import com.example.core.model.MusicModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive

class MusicServiceConnection(
  private var context: Context,
  private var coroutineScope: CoroutineScope,
) {

  private var factory: ListenableFuture<MediaController>? = null
  var mediaController: MediaController? = null

  private var _playerState = MutableStateFlow(PlayerStateModel.Empty)
  var playerState: StateFlow<PlayerStateModel> = _playerState.asStateFlow()

  private var _currentArtworkPagerIndex = MutableStateFlow(0)
  var currentArtworkPagerIndex: StateFlow<Int> = _currentArtworkPagerIndex.asStateFlow()

  private var _artworkPagerList = MutableStateFlow(listOf<MusicModel>())
  var artworkPagerList = _artworkPagerList.asStateFlow()

  private var _currentMusicPosition = MutableStateFlow(0L)
  var currentMusicPosition =
    _currentMusicPosition
      .onStart {
        musicPosition
          .onEach {
            _currentMusicPosition.value = it
          }.launchIn(coroutineScope)
      }

  init {
    initialExoPlayer()
  }

  private var musicPosition: Flow<Long> = flow {
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

  fun pauseMusic() = mediaController?.pause()

  fun resumeMusic() = mediaController?.play()

  fun seekToPosition(position: Long) {
    mediaController?.seekTo(position)
    _currentMusicPosition.update { position }
  }

  fun moveToMediaIndex(index: Int) {
    _currentMusicPosition.update { 0 }
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

  fun setRepeatMode(repeatMode: Int) = mediaController?.repeatMode = repeatMode

  fun setCurrentArtworkPagerIndex(index: Int) = _currentArtworkPagerIndex.update { index }

  fun moveToNext() {
    val hasNextItem = mediaController?.hasNextMediaItem() == true
    if (!hasNextItem) return
    mediaController?.apply {
      seekToNext()
      prepare()
      play()
    }
    _currentMusicPosition.update { 0 }
    _currentArtworkPagerIndex.update { it + 1 }
  }

  fun moveToPrevious(seekToStart: Boolean = false) {
    val hasPreviewItem = mediaController?.hasNextMediaItem() == true
    if (!hasPreviewItem) return

    _currentMusicPosition.update { 0 }
    if (_currentMusicPosition.value <= 15_000 || seekToStart) {
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

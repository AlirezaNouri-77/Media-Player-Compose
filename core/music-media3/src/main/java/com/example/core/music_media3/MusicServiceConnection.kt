package com.example.core.music_media3

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.core.model.CurrentMusicInfo
import com.example.core.model.MusicModel
import com.example.core.model.MusicPlayerState
import com.example.core.model.toRepeatMode
import com.example.core.music_media3.mapper.toActiveMusicInfo
import com.example.core.music_media3.mapper.toArtworkModel
import com.example.core.music_media3.mapper.toMediaItem
import com.example.core.music_media3.mapper.toMusicModel
import com.example.core.music_media3.model.ArtworkModel
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds

class MusicServiceConnection(
    private var context: Context,
) {
    private var factory: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    private var _playerState = MutableStateFlow(MusicPlayerState.Initial)
    var playerState = _playerState.asStateFlow()

    private var _currentPlayedMusicList = MutableStateFlow(emptyList<MusicModel>())
    var currentPlayedMusicList = _currentPlayedMusicList.asStateFlow()

    init {
        initialExoPlayer()
    }

    private fun initialExoPlayer() {
        if (factory == null) {
            factory = MediaController.Builder(
                context,
                SessionToken(
                    context,
                    ComponentName(context, MusicPlayerService::class.java),
                ),
            ).buildAsync().also { mediaControllerFuture ->
                mediaControllerFuture.addListener(
                    {
                        mediaController = factory?.get().also { it?.addListener(exoPlayerListener) }
                        _playerState.update {
                            it.copy(currentMusicInfo = mediaController?.currentMediaItem?.toActiveMusicInfo() ?: CurrentMusicInfo.Initial)
                        }
                    },
                    ContextCompat.getMainExecutor(context),
                )
            }
        }
    }

    fun updateCurrentMusicFavorite(isFavorite: Boolean) {
        val currentMediaItem = mediaController?.currentMediaItem
        val updatedFavoriteMusic = currentMediaItem?.toMusicModel()?.copy(isFavorite = isFavorite)
        updatedFavoriteMusic?.let {
            mediaController?.replaceMediaItem(
                mediaController?.currentMediaItemIndex ?: 0,
                updatedFavoriteMusic.toMediaItem(),
            )
        }
    }

    val currentPlayerPosition = flow {
        while (currentCoroutineContext().isActive) {
            if (mediaController?.isPlaying ?: false) {
                val position = mediaController?.currentPosition ?: 0L
                emit(position)
            }
            delay(80L.milliseconds)
        }
    }

    fun pauseMusic() = mediaController?.pause()

    fun resumeMusic() = mediaController?.play()

    fun seekToPosition(position: Long) = mediaController?.seekTo(position)

    fun moveToMediaIndex(index: Int) {
        mediaController?.apply {
            seekTo(index, 0L)
            prepare()
            play()
        }
    }

    fun playSongs(index: Int, musicList: List<MusicModel>) {
        _currentPlayedMusicList.update { musicList }
        mediaController?.run {
            setMediaItems(musicList.map(MusicModel::toMediaItem), index, 0L)
            prepare()
            play()
        }
    }

    fun setRepeatMode(repeatMode: Int) = mediaController?.repeatMode = repeatMode

    fun setShuffleMode(boolean: Boolean) = mediaController?.shuffleModeEnabled = boolean

    fun moveToNext() {
        val hasNextItem = mediaController?.hasNextMediaItem() == true
        if (!hasNextItem) return
        mediaController?.apply {
            seekToNext()
            prepare()
            play()
        }
    }

    fun moveToPrevious(seekToStart: Boolean = false, currentMusicPosition: Long) {
        val hasPreviewItem = mediaController?.hasNextMediaItem() == true
        if (!hasPreviewItem) return

        mediaController?.apply {
            if (currentMusicPosition <= 15_000 || seekToStart) seekToPreviousMediaItem() else seekToPrevious()
            prepare()
            play()
        }
    }

    private val exoPlayerListener = object : Player.Listener {
        override fun onRepeatModeChanged(repeatMode: Int) {
            _playerState.update { it.copy(playerRepeatMode = repeatMode.toRepeatMode()) }
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            _playerState.update { it.copy(isPlaying = playWhenReady) }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            _playerState.update {
                _playerState.value.copy(
                    currentMusicInfo = mediaItem?.toActiveMusicInfo() ?: CurrentMusicInfo.Initial,
                )
            }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            _playerState.update {
                it.copy(isShuffleMode = mediaController?.shuffleModeEnabled ?: false)
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

    fun getMediaItemsList(): List<ArtworkModel> {
        val timeLine = mediaController?.currentTimeline
        var nextIndex =
            timeLine?.getFirstWindowIndex(mediaController?.shuffleModeEnabled ?: false) ?: -1

        return buildList {
            while (nextIndex != C.INDEX_UNSET) {
                mediaController?.getMediaItemAt(nextIndex)
                    ?.let { this.add(it.toArtworkModel()) }
                nextIndex = timeLine?.getNextWindowIndex(
                    nextIndex,
                    Player.REPEAT_MODE_OFF,
                    mediaController?.shuffleModeEnabled ?: false,
                ) ?: 0
            }
        }
    }
}

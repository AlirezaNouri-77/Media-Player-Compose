package com.example.video_media3

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.core.model.ActiveVideoInfo
import com.example.core.model.VideoModel
import com.example.video_media3.model.VideoPlayerState
import com.example.video_media3.model.mapper.toActiveVideoInfo
import com.example.video_media3.model.mapper.toMediaItem
import com.example.video_media3.util.VideoMediaMetaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VideoMedia3Controller(
    private var videoMediaMetaData: VideoMediaMetaData,
    private var exoPlayer: ExoPlayer,
) {

    private var _videoPlayerStateStateFlow = MutableStateFlow(VideoPlayerState.Empty)
    val videoPlayerStateStateFlow: StateFlow<VideoPlayerState> =
        _videoPlayerStateStateFlow.asStateFlow()

    val getPlayer: ExoPlayer = exoPlayer

    private lateinit var exoPlayerEventListener: Player.Listener

    fun startPlay(index: Int, videoList: List<VideoModel>) {
        exoPlayer.apply {
            this.setMediaItems(videoList.map(VideoModel::toMediaItem), index, 0L)
            this.playWhenReady = true
            this.prepare()
            this.play()
        }
    }

    fun resumePlayer() = exoPlayer.play()

    fun seekToNextMediaItem() = exoPlayer.seekToNextMediaItem()

    fun fastForward(position: Long, currentPosition: Long) =
        exoPlayer.seekTo(currentPosition + position)

    fun fastRewind(position: Long, currentPosition: Long) =
        exoPlayer.seekTo(currentPosition - position)

    fun pausePlayer() = exoPlayer.pause()

    fun seekToPreviousMediaItem() = exoPlayer.seekToPreviousMediaItem()

    fun stop() {
        exoPlayer.pause()
        exoPlayer.clearMediaItems()
    }

    fun seekTo(position: Long) = exoPlayer.seekTo(position)

    suspend fun startPlayFromUri(uri: Uri) {
        val metaData = videoMediaMetaData.get(uri)
        metaData?.let {
            exoPlayer.apply {
                this.addMediaItem(metaData.toMediaItem())
                this.playWhenReady = true
                this.prepare()
                this.play()
            }
        }
    }

    fun unRegisterMedia3Listener() = exoPlayer.removeListener(exoPlayerEventListener)

    fun registerMedia3Listener() {

        exoPlayerEventListener = object : Player.Listener {

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                _videoPlayerStateStateFlow.update { it.copy(isPlaying = playWhenReady) }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> _videoPlayerStateStateFlow.update {
                        it.copy(
                            isBuffering = true
                        )
                    }

                    Player.STATE_READY -> _videoPlayerStateStateFlow.update { it.copy(isBuffering = false) }
                    else -> {}
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                if (mediaItem == null) return

                _videoPlayerStateStateFlow.update {
                    it.copy(currentMediaInfo = mediaItem.toActiveVideoInfo())
                }
            }

        }

        exoPlayer.addListener(exoPlayerEventListener)
        _videoPlayerStateStateFlow.update {
            VideoPlayerState(
                currentMediaInfo = exoPlayer.currentMediaItem?.toActiveVideoInfo()
                    ?: ActiveVideoInfo.Empty,
                isPlaying = true,
                isBuffering = true,
                repeatMode = exoPlayer.repeatMode,
            )
        }

    }

}
package com.example.core.music_media3

import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MusicPlayerService : MediaSessionService() {

  private var mediaSession: MediaSession? = null

  override fun onCreate() {
    super.onCreate()
    val musicPlayer = ExoPlayer.Builder(this).build()
    mediaSession = MediaSession.Builder(this, musicPlayer).build()
  }

  override fun onTaskRemoved(rootIntent: Intent?) {
    mediaSession?.let {
      if (!it.player.isPlaying) {
        stopSelf()
      }
    }
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
    return mediaSession
  }

  override fun onDestroy() {
    super.onDestroy()
    mediaSession?.player?.release()
  }

}
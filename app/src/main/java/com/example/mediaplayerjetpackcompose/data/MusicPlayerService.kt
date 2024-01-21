package com.example.mediaplayerjetpackcompose.data

import android.content.Intent
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class MusicPlayerService : MediaSessionService(){
  
  private var mediaSession: MediaSession? = null
  
  override fun onCreate() {
	super.onCreate()
	val musicPlayer = ExoPlayer.Builder(this).build()
	mediaSession = MediaSession.Builder(this, musicPlayer).build()
  }
  
  override fun onTaskRemoved(rootIntent: Intent?) {
	mediaSession?.let {
	  if (!it.player.isPlaying){
		stopSelf()
	  }
	}
  }
  
  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
	return mediaSession
  }
  
  override fun onDestroy() {
	mediaSession?.run {
	  player.release()
	  release()
	  mediaSession = null
	}
	super.onDestroy()
  }
  
}
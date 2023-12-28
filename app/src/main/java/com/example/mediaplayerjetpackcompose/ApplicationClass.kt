package com.example.mediaplayerjetpackcompose

import android.app.Application
import androidx.media3.exoplayer.ExoPlayer
import com.example.mediastore.data.MediaStoreRepository

class ApplicationClass : Application() {
  
  lateinit var mediaStoreRepository: MediaStoreRepository
  lateinit var exoPlayer: ExoPlayer
  
  override fun onCreate() {
	super.onCreate()
	mediaStoreRepository = MediaStoreRepository()
	exoPlayer = ExoPlayer.Builder(this).build()
  }
  
  
}
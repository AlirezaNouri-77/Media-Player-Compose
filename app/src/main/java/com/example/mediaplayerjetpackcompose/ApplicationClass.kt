package com.example.mediaplayerjetpackcompose

import android.app.Application
import androidx.media3.exoplayer.ExoPlayer
import com.example.mediastore.data.MediaStoreRepository

class ApplicationClass : Application() {
  
  lateinit var mediaStoreRepository: MediaStoreRepository
  
  override fun onCreate() {
	super.onCreate()
	mediaStoreRepository = MediaStoreRepository()
  }
  
}
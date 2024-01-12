package com.example.mediaplayerjetpackcompose

import android.app.Application
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository

class ApplicationClass : Application() {
  
  lateinit var videoMediaStoreRepository: VideoMediaStoreRepository
  lateinit var musicMediaStoreRepository: MusicMediaStoreRepository
  
  override fun onCreate() {
	super.onCreate()
	videoMediaStoreRepository = VideoMediaStoreRepository()
	musicMediaStoreRepository = MusicMediaStoreRepository()
  }
  
}
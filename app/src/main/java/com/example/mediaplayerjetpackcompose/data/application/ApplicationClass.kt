package com.example.mediaplayerjetpackcompose.data.application

import android.app.Application
import com.example.mediaplayerjetpackcompose.data.GetMediaArt
import com.example.mediaplayerjetpackcompose.data.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository

class ApplicationClass : Application() {

  lateinit var videoMediaStoreRepository: VideoMediaStoreRepository
  lateinit var musicMediaStoreRepository: MusicMediaStoreRepository
  lateinit var getMediaArt: GetMediaArt
  lateinit var musicServiceConnection: MusicServiceConnection

  override fun onCreate() {
    super.onCreate()
    getMediaArt = GetMediaArt(context = this, contentResolver = this.contentResolver)
    videoMediaStoreRepository = VideoMediaStoreRepository(contentResolver = this.contentResolver)
    musicMediaStoreRepository =
      MusicMediaStoreRepository(contentResolver = this.contentResolver, getMediaArt = getMediaArt)
    musicServiceConnection = MusicServiceConnection(this)
  }

}
package com.example.mediaplayerjetpackcompose.data.application

import android.app.Application
import com.example.mediaplayerjetpackcompose.data.util.GetMediaArt
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.database.databaseClass.AppDataBase
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository

class ApplicationClass : Application() {

  lateinit var videoMediaStoreRepository: VideoMediaStoreRepository
  lateinit var musicMediaStoreRepository: MusicMediaStoreRepository
  lateinit var appDataBase: AppDataBase
  lateinit var getMediaArt: GetMediaArt
  lateinit var musicServiceConnection: MusicServiceConnection

  override fun onCreate() {
    super.onCreate()
    appDataBase = AppDataBase.getInstance(this)
    getMediaArt = GetMediaArt(context = this, contentResolver = this.contentResolver)
    videoMediaStoreRepository = VideoMediaStoreRepository(contentResolver = this.contentResolver)
    musicMediaStoreRepository = MusicMediaStoreRepository(contentResolver = this.contentResolver, getMediaArt = getMediaArt,)
    musicServiceConnection = MusicServiceConnection(this)
  }

}
package com.example.core.music_media3.di

import com.example.core.music_media3.MusicServiceConnection
import com.example.core.music_media3.util.DeviceVolumeManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

var Media3Module = module {

  single {
    MusicServiceConnection(androidApplication().applicationContext)
  }

  single { DeviceVolumeManager(androidApplication().applicationContext) }
}
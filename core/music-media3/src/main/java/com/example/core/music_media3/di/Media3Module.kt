package com.example.core.music_media3.di

import com.example.core.music_media3.MusicServiceConnection
import com.example.core.music_media3.util.DeviceVolumeManager
import com.example.core.music_media3.util.MusicThumbnailUtil
import com.example.core.music_media3.util.MusicThumbnailUtilImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var Media3Module = module {

  single {
    MusicServiceConnection(androidApplication().applicationContext, get(named("CoroutineMain")))
  }

  single {
    MusicThumbnailUtil(
      androidApplication().applicationContext,
      get(named("Default")),
      get(named("IO"))
    )
  } bind MusicThumbnailUtilImpl::class

  single { DeviceVolumeManager(androidApplication().applicationContext) }

}
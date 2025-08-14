package com.example.core.music_media3.di

import com.example.core.common.di.DispatcherType
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
    MusicServiceConnection(androidApplication().applicationContext)
  }

  single {
    MusicThumbnailUtil(
      androidApplication().applicationContext,
      get(DispatcherType.DEFAULT.qualifier),
      get(DispatcherType.IO.qualifier)
    )
  } bind MusicThumbnailUtilImpl::class

  single { DeviceVolumeManager(androidApplication().applicationContext) }

}
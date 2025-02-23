package com.example.mediaplayerjetpackcompose.core.data.di

import com.example.mediaplayerjetpackcompose.core.data.DeviceVolumeManager
import com.example.mediaplayerjetpackcompose.core.data.FavoriteMusicManager
import com.example.mediaplayerjetpackcompose.core.data.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.core.data.SearchMusicManager
import com.example.mediaplayerjetpackcompose.core.data.VideoMediaVideoMediaMetaData
import com.example.mediaplayerjetpackcompose.core.domain.api.FavoriteMusicManagerImpl
import com.example.mediaplayerjetpackcompose.core.domain.api.MediaThumbnailUtilImpl
import com.example.mediaplayerjetpackcompose.core.domain.api.SearchMusicManagerImpl
import com.example.mediaplayerjetpackcompose.core.domain.api.VideoMediaMetaDataImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var DataModule = module {

  single { MediaThumbnailUtil(androidApplication().applicationContext,get(named("Default")),get(named("IO"))) } bind MediaThumbnailUtilImpl::class
  single { DeviceVolumeManager(androidApplication().applicationContext) }
  single { VideoMediaVideoMediaMetaData(get(),get(named("IO"))) } bind VideoMediaMetaDataImpl::class
  single { SearchMusicManager(get()) } bind SearchMusicManagerImpl::class
  single { FavoriteMusicManager(get(),get(named("IO"))) } bind FavoriteMusicManagerImpl::class

}
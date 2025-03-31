package com.example.core.data.di

import com.example.core.data.repository.FavoriteMusicSource
import com.example.core.data.util.DeviceVolumeManager
import com.example.core.data.util.MediaThumbnailUtil
import com.example.core.data.util.SearchMusicManager
import com.example.core.domain.api.FavoriteMusicSourceImpl
import com.example.core.domain.api.MediaThumbnailUtilImpl
import com.example.core.domain.api.SearchMusicManagerImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var DataModule = module {

  single {
    MediaThumbnailUtil(
      androidApplication().applicationContext,
      get(named("Default")),
      get(named("IO"))
    )
  } bind MediaThumbnailUtilImpl::class

  single { DeviceVolumeManager(androidApplication().applicationContext) }

  single { SearchMusicManager(get(), get(named("IO"))) } bind SearchMusicManagerImpl::class

  single { FavoriteMusicSource(get(), get(), get(named("IO"))) } bind FavoriteMusicSourceImpl::class

  single {
    FavoriteMusicSource(
      get(),
      get(),
      get(named("IO"))
    )
  } bind FavoriteMusicSource::class

}
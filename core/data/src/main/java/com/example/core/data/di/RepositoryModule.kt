package com.example.core.data.di

import com.example.core.data.repository.MediaStoreRepository
import com.example.core.data.repository.MusicSource
import com.example.core.data.repository.VideoSource
import com.example.core.domain.api.MusicRepositoryImpl
import com.example.core.domain.api.MusicSourceImpl
import com.example.core.domain.api.VideoSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var RepositoryModule = module {

  single {
    MediaStoreRepository(androidApplication().applicationContext.contentResolver)
  } bind MusicRepositoryImpl::class

  single {
    VideoSource(androidApplication().applicationContext.contentResolver)
  } bind VideoSourceImpl::class

  single { MusicSource(get(), get(named("IO"))) } bind MusicSourceImpl::class

}
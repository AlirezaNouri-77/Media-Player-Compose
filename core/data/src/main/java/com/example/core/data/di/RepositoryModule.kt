package com.example.core.data.di

import com.example.core.data.repository.MusicRepository
import com.example.core.domain.respository.MusicRepositoryImpl
import com.example.core.data.repository.MusicSource
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.data.repository.VideoRepository
import com.example.core.domain.respository.VideoRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var RepositoryModule = module {

  single {
    MusicRepository(androidApplication().applicationContext.contentResolver)
  } bind MusicRepositoryImpl::class

  single {
    VideoRepository(androidApplication().applicationContext.contentResolver)
  } bind VideoRepositoryImpl::class

  single { MusicSource(get(), get(named("IO"))) } bind MusicSourceImpl::class

}
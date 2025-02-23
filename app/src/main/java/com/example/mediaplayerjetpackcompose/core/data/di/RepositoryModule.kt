package com.example.mediaplayerjetpackcompose.core.data.di

import com.example.mediaplayerjetpackcompose.core.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.core.data.repository.MusicSourceRepository
import com.example.mediaplayerjetpackcompose.core.data.repository.VideoVideoRepository
import com.example.mediaplayerjetpackcompose.core.domain.api.VideoRepositoryImpl
import com.example.mediaplayerjetpackcompose.core.domain.api.MusicRepositoryImpl
import com.example.mediaplayerjetpackcompose.core.domain.api.MusicSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import org.koin.dsl.module

var RepositoryModule = module {

  single {
    MusicMediaStoreRepository(androidApplication().applicationContext.contentResolver)
  } bind MusicRepositoryImpl::class

  single {
    VideoVideoRepository(androidApplication().applicationContext.contentResolver)
  } bind VideoRepositoryImpl::class

  single { MusicSourceRepository(get(), get()) } bind MusicSourceImpl::class

}
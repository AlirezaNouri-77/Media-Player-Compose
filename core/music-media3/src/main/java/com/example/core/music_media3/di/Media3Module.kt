package com.example.core.music_media3.di

import com.example.core.music_media3.MusicServiceConnection
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

var Media3Module = module {

  single {
    MusicServiceConnection(androidApplication().applicationContext, get(named("CoroutineMain")))
  }

}
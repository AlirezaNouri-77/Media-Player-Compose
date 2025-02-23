package com.example.mediaplayerjetpackcompose.core.musicPlayer.di

import com.example.mediaplayerjetpackcompose.core.musicPlayer.MusicServiceConnection
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

var Media3Module = module {

  single {
    MusicServiceConnection(androidApplication().applicationContext, get(named("CoroutineMain")))
  }

}
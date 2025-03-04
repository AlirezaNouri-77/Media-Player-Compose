package com.example.feature.video.di

import androidx.media3.exoplayer.ExoPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

var VideoModule = module {

  factory { ExoPlayer.Builder(androidApplication().applicationContext).build() }

}
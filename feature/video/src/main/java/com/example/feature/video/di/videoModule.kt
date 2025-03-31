package com.example.feature.video.di

import androidx.media3.exoplayer.ExoPlayer
import com.example.feature.video.VideoPageViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var VideoModule = module {

  viewModelOf(::VideoPageViewModel)

}
package com.example.feature.video.di

import com.example.feature.video.VideoPageViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var VideoModule = module {

  viewModelOf(::VideoPageViewModel)

}
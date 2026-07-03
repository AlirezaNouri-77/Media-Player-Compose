package com.shermanrex.feature.video.di

import com.shermanrex.feature.video.VideoViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var VideoModule = module {

    viewModelOf(::VideoViewModel)
}

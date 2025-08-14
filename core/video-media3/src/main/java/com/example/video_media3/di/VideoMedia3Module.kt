package com.example.video_media3.di

import androidx.media3.exoplayer.ExoPlayer
import com.example.core.common.di.DispatcherType
import com.example.video_media3.VideoMedia3Controller
import com.example.video_media3.model.VideoMediaMetaDataImpl
import com.example.video_media3.model.VideoThumbnailUtilImpl
import com.example.video_media3.util.VideoMediaMetaData
import com.example.video_media3.util.VideoThumbnailUtil
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var VideoMedia3Module = module {

  single {
    ExoPlayer.Builder(androidApplication().applicationContext).build()
  }

  single { VideoMediaMetaData(androidApplication().applicationContext, get(DispatcherType.IO.qualifier)) } bind VideoMediaMetaDataImpl::class
  single { VideoThumbnailUtil(get(DispatcherType.DEFAULT.qualifier),androidApplication().applicationContext) } bind VideoThumbnailUtilImpl::class

  single { VideoMedia3Controller(get(), get()) }

}
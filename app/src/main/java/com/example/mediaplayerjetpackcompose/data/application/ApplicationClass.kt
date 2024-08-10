package com.example.mediaplayerjetpackcompose.data.application

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.util.DebugLogger
import com.example.mediaplayerjetpackcompose.data.di.appModule
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ApplicationClass : Application(), ImageLoaderFactory {

  override fun onCreate() {
    super.onCreate()
    startKoin {
      allowOverride(true)
      androidContext(this@ApplicationClass)
      androidLogger(Level.INFO)
      modules(appModule)
    }
  }

  override fun newImageLoader(): ImageLoader {
    return ImageLoader(this).newBuilder().apply {
      components {
        add(VideoFrameDecoder.Factory())
      }
      diskCache {
        DiskCache
          .Builder().run {
            maxSizePercent(0.1)
            directory(this@ApplicationClass.cacheDir.resolve("image_cache"))
            cleanupDispatcher(Dispatchers.IO)
            build()
          }
      }
      crossfade(true)
      logger(DebugLogger())
    }.build()

  }
}
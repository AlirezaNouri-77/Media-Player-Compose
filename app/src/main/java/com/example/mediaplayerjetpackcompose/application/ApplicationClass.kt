package com.example.mediaplayerjetpackcompose.application

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.util.DebugLogger
import com.example.mediaplayerjetpackcompose.core.common.coroutineModule.CoroutineModule
import com.example.mediaplayerjetpackcompose.core.common.coroutineModule.DispatcherModule
import com.example.mediaplayerjetpackcompose.core.data.di.DataModule
import com.example.mediaplayerjetpackcompose.core.musicPlayer.di.Media3Module
import com.example.mediaplayerjetpackcompose.core.data.di.RepositoryModule
import com.example.mediaplayerjetpackcompose.core.database.di.DataBaseDaoModule
import com.example.mediaplayerjetpackcompose.core.database.di.DataBaseModule
import com.example.mediaplayerjetpackcompose.di.appModule
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
      modules(
        appModule,
        CoroutineModule,
        DispatcherModule,
        DataBaseModule,
        DataBaseDaoModule,
        RepositoryModule,
        Media3Module,
        DataModule,
      )
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
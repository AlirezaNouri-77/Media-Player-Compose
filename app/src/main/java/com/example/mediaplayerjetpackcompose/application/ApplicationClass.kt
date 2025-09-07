package com.example.mediaplayerjetpackcompose.application

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.util.DebugLogger
import com.example.core.common.di.CommonCoroutineModule
import com.example.core.common.di.CommonDispatcherModule
import com.example.core.data.di.DataModule
import com.example.core.database.di.DataBaseModule
import com.example.core.domain.di.useCaseModule
import com.example.core.music_media3.di.MusicMedia3Module
import com.example.datastore.di.dataStoreModule
import com.example.feature.video.di.VideoModule
import com.example.mediaplayerjetpackcompose.di.appModule
import com.example.video_media3.di.VideoMedia3Module
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
            androidLogger(Level.ERROR)
            modules(
                appModule,
                CommonCoroutineModule,
                CommonDispatcherModule,
                DataModule,
                DataBaseModule,
                VideoModule,
                MusicMedia3Module,
                VideoMedia3Module,
                dataStoreModule,
                useCaseModule,
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

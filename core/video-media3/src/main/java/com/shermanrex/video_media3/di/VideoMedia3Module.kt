package com.shermanrex.video_media3.di

import android.annotation.SuppressLint
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import com.shermanrex.core.common.DispatcherType
import com.shermanrex.core.common.ExoPlayerType
import com.shermanrex.video_media3.VideoMedia3Controller
import com.shermanrex.video_media3.model.VideoMediaMetaDataImpl
import com.shermanrex.video_media3.model.VideoThumbnailUtilImpl
import com.shermanrex.video_media3.util.VideoMediaMetaData
import com.shermanrex.video_media3.util.VideoThumbnailUtil
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
var VideoMedia3Module = module {

    single(ExoPlayerType.VIDEO.qualifier) {
        ExoPlayer.Builder(androidApplication().applicationContext)
            .setSeekParameters(SeekParameters.EXACT)
            .setHandleAudioBecomingNoisy(true)
            .build()
    }

    single {
        VideoMediaMetaData(
            androidApplication().applicationContext,
            get(DispatcherType.IO.qualifier),
        )
    } bind VideoMediaMetaDataImpl::class
    single {
        VideoThumbnailUtil(
            get(DispatcherType.DEFAULT.qualifier),
            androidApplication().applicationContext,
        )
    } bind VideoThumbnailUtilImpl::class

    single { VideoMedia3Controller(get(), get((ExoPlayerType.VIDEO.qualifier))) }
}

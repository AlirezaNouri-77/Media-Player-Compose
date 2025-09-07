package com.example.core.common.di

import com.example.core.common.DispatcherType
import com.example.core.common.MusicThumbnailUtilImpl
import com.example.core.common.util.MusicThumbnailUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var CommonCoroutineModule = module {

    factory(named("CoroutineMain")) {
        CoroutineScope(SupervisorJob() + get<MainCoroutineDispatcher>(DispatcherType.MAIN.qualifier))
    }

    factory(named("CoroutineIO")) {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(DispatcherType.IO.qualifier))
    }

    factory(named("CoroutineDefault")) {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(DispatcherType.DEFAULT.qualifier))
    }

    single {
        MusicThumbnailUtil(
            androidApplication().applicationContext,
            get(DispatcherType.DEFAULT.qualifier),
            get(DispatcherType.IO.qualifier),
        )
    } bind MusicThumbnailUtilImpl::class
}

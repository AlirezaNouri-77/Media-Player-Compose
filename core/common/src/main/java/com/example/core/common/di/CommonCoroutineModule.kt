package com.example.core.common.di

import com.example.core.common.util.MusicThumbnailUtil
import com.example.core.domain.respository.MusicThumbnailUtilImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.StringQualifier
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

enum class CoroutineType(val qualifier: StringQualifier) {
    MAIN(StringQualifier("CoroutineMain")),
    IO(StringQualifier("CoroutineIO")),
    DEFAULT(StringQualifier("CoroutineDefault")),
}

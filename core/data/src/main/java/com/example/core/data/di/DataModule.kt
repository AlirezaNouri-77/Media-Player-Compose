package com.example.core.data.di

import com.example.core.common.DispatcherType
import com.example.core.data.repository.FavoriteRepository
import com.example.core.data.repository.MusicRepository
import com.example.core.data.repository.MusicSource
import com.example.core.data.repository.SearchMusicRepository
import com.example.core.data.repository.VideoRepository
import com.example.core.data.util.MusicThumbnailUtil
import com.example.core.domain.MusicThumbnailUtilImpl
import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.core.domain.respository.MusicRepositoryImpl
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.domain.respository.SearchMusicRepositoryImpl
import com.example.core.domain.respository.VideoRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import org.koin.dsl.module

var DataModule = module {

    single {
        SearchMusicRepository(get(), get(DispatcherType.IO.qualifier))
    } bind SearchMusicRepositoryImpl::class

    single {
        FavoriteRepository(get(), get(DispatcherType.IO.qualifier))
    } bind FavoriteRepositoryImpl::class

    single {
        MusicRepository(androidApplication().applicationContext)
    } bind MusicRepositoryImpl::class

    single {
        VideoRepository(androidApplication().applicationContext.contentResolver)
    } bind VideoRepositoryImpl::class

    single {
        MusicSource(get(), get(), get(DispatcherType.IO.qualifier))
    } bind MusicSourceImpl::class

    single {
        MusicThumbnailUtil(
            androidApplication().applicationContext,
            get(DispatcherType.DEFAULT.qualifier),
            get(DispatcherType.IO.qualifier),
        )
    } bind MusicThumbnailUtilImpl::class
}

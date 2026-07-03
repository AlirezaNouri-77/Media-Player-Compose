package com.shermanrex.core.data.di

import com.shermanrex.core.common.DispatcherType
import com.shermanrex.core.data.repository.FavoriteRepository
import com.shermanrex.core.data.repository.MusicRepository
import com.shermanrex.core.data.repository.MusicSource
import com.shermanrex.core.data.repository.SearchMusicRepository
import com.shermanrex.core.data.repository.VideoRepository
import com.shermanrex.core.data.util.MusicThumbnailUtil
import com.shermanrex.core.domain.MusicThumbnailUtilImpl
import com.shermanrex.core.domain.respository.FavoriteRepositoryImpl
import com.shermanrex.core.domain.respository.MusicRepositoryImpl
import com.shermanrex.core.domain.respository.MusicSourceImpl
import com.shermanrex.core.domain.respository.SearchMusicRepositoryImpl
import com.shermanrex.core.domain.respository.VideoRepositoryImpl
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

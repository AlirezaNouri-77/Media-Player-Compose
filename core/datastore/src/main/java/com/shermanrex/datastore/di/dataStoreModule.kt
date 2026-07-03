package com.shermanrex.datastore.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.shermanrex.core.common.DispatcherType
import com.shermanrex.core.model.datastore.CategorizedSortModel
import com.shermanrex.core.model.datastore.SongSortModel
import com.shermanrex.datastore.AlbumSortDataStoreManager
import com.shermanrex.datastore.ArtistSortDataStoreManager
import com.shermanrex.datastore.FolderSortDataStoreManager
import com.shermanrex.datastore.ScrollListDataStoreManager
import com.shermanrex.datastore.SongsSortDataStoreManager
import com.shermanrex.datastore.SortDataStoreManagerImpl
import com.shermanrex.datastore.serializer.MyProtoPreferencesSerializer
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

var dataStoreModule = module {

    single {
        MyProtoPreferencesSerializer()
    }

    single {
        DataStoreFactory.create(
            serializer = get<MyProtoPreferencesSerializer>(),
            scope = get<CoroutineScope>(named("CoroutineIO")),
        ) {
            androidApplication().applicationContext.dataStoreFile("DataStore.db")
        }
    }

    single {
        ScrollListDataStoreManager(get(), get(DispatcherType.DEFAULT.qualifier))
    }
    single<SortDataStoreManagerImpl<SongSortModel>>(named("SongsSortDataStore")) {
        SongsSortDataStoreManager(get(), get(DispatcherType.DEFAULT.qualifier))
    }

    single<SortDataStoreManagerImpl<CategorizedSortModel>>(named("ArtistSortDataStore")) {
        ArtistSortDataStoreManager(get(), get(DispatcherType.DEFAULT.qualifier))
    }

    single<SortDataStoreManagerImpl<CategorizedSortModel>>(named("AlbumSortDataStore")) {
        AlbumSortDataStoreManager(get(), get(DispatcherType.DEFAULT.qualifier))
    }

    single<SortDataStoreManagerImpl<CategorizedSortModel>>(named("FolderSortDataStore")) {
        FolderSortDataStoreManager(get(), get(DispatcherType.DEFAULT.qualifier))
    }
}

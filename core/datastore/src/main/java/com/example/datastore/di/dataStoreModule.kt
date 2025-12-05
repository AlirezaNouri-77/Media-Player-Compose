package com.example.datastore.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.core.common.DispatcherType
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.SongSortModel
import com.example.datastore.AlbumSortDataStoreManager
import com.example.datastore.ArtistSortDataStoreManager
import com.example.datastore.FolderSortDataStoreManager
import com.example.datastore.ScrollListDataStoreManager
import com.example.datastore.SongsSortDataStoreManager
import com.example.datastore.SortDataStoreManagerImpl
import com.example.datastore.serializer.MyProtoPreferencesSerializer
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

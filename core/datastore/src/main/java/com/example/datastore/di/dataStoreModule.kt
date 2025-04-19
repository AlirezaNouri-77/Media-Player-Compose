package com.example.datastore.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.core.model.CategorizedSortModel
import com.example.core.model.FolderSortType
import com.example.core.model.SongSortModel
import com.example.core.model.SongsSortType
import com.example.core.model.SortStateModel
import com.example.datastore.AlbumSortDataStoreManager
import com.example.datastore.ArtistSortDataStoreManager
import com.example.datastore.FolderSortDataStoreManager
import com.example.datastore.SongsSortDataStoreManager
import com.example.datastore.SortDataStoreManagerImpl
import com.example.datastore.serializer.SortPreferencesSerializer
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

var dataStoreModule = module {

  single {
    SortPreferencesSerializer()
  }

  single {
    DataStoreFactory.create(
      serializer = get<SortPreferencesSerializer>(),
      scope = get<CoroutineScope>(named("CoroutineIO")),
    ) {
      androidApplication().applicationContext.dataStoreFile("SortDataStore.db")
    }
  }

  single<SortDataStoreManagerImpl<SongSortModel>>(named("SongsSortDataStore")) {
    SongsSortDataStoreManager(get(), get(named("IO")))
  }

  single<SortDataStoreManagerImpl<CategorizedSortModel>>(named("ArtistSortDataStore")) {
    ArtistSortDataStoreManager(get(), get(named("IO")))
  }

  single<SortDataStoreManagerImpl<CategorizedSortModel>>(named("AlbumSortDataStore")) {
    AlbumSortDataStoreManager(get(), get(named("IO")))
  }

  single<SortDataStoreManagerImpl<CategorizedSortModel>>(named("FolderSortDataStore")) {
    FolderSortDataStoreManager(get(), get(named("IO")))
  }

}
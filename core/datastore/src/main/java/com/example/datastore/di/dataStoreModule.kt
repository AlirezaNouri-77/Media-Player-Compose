package com.example.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.datastore.DataStoreManager
import com.example.datastore.SortPreferencesSerializer
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
      androidApplication().applicationContext.dataStoreFile("sss.db")
    }
  }

  single {
    DataStoreManager(get())
  }

}
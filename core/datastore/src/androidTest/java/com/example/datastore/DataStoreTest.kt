package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.model.SortType
import com.example.core.proto_datastore.SortPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DataStoreTest {

  @get:Rule
  val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

  private val testContext: Context = ApplicationProvider.getApplicationContext()

  var dispatcher = UnconfinedTestDispatcher()
  var coroutine = CoroutineScope(dispatcher)


  var testDataStore: DataStore<SortPreferences> = DataStoreFactory.create(
    serializer = SortPreferencesSerializer(),
    produceFile = {
      testContext.dataStoreFile(tmpFolder.newFolder().absolutePath)
    },
    scope = coroutine
  )

  var dataStoreManager = DataStoreManager(testDataStore)

  @Test
  fun getSongsSortState() = runTest {

    dataStoreManager.updateSongsSortType(SortType.DURATION)
    dataStoreManager.updateSongsIsDescending(true)

    var data = dataStoreManager.songsSortState.first()

    assertTrue(data.isDec)
    assertEquals(SortType.DURATION, data.sortType)

  }

  @Test
  fun checkUpdateSongsDataStoreMethod() = runTest {

    dataStoreManager.updateSongsSortType(SortType.DURATION)
    dataStoreManager.updateSongsIsDescending(true)

    var beforeUpdate = dataStoreManager.songsSortState.first()

    assertTrue(beforeUpdate.isDec)
    assertEquals(SortType.DURATION, beforeUpdate.sortType)

    dataStoreManager.updateSongsSortType(SortType.SIZE)
    dataStoreManager.updateSongsIsDescending(false)

    var afterUpdate = dataStoreManager.songsSortState.first()

    assertFalse(afterUpdate.isDec)
    assertEquals(SortType.SIZE, afterUpdate.sortType)

  }

}
package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.model.SortType
import com.example.core.proto_datastore.SortPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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

  var songsSortDataStoreManager = SongsSortDataStoreManager(testDataStore)

  @Test
  fun getSongsSortState() = runTest {

    songsSortDataStoreManager.updateSongsSortType(SortType.DURATION)
    songsSortDataStoreManager.updateSongsIsDescending(true)

    var data = songsSortDataStoreManager.songsSortState.first()

    assertTrue(data.isDec)
    assertEquals(SortType.DURATION, data.sortType)

  }

  @Test
  fun checkUpdateSongsDataStoreMethod() = runTest {

    songsSortDataStoreManager.updateSongsSortType(SortType.DURATION)
    songsSortDataStoreManager.updateSongsIsDescending(true)

    var beforeUpdate = songsSortDataStoreManager.songsSortState.first()

    assertTrue(beforeUpdate.isDec)
    assertEquals(SortType.DURATION, beforeUpdate.sortType)

    songsSortDataStoreManager.updateSongsSortType(SortType.SIZE)
    songsSortDataStoreManager.updateSongsIsDescending(false)

    var afterUpdate = songsSortDataStoreManager.songsSortState.first()

    assertFalse(afterUpdate.isDec)
    assertEquals(SortType.SIZE, afterUpdate.sortType)

  }

}
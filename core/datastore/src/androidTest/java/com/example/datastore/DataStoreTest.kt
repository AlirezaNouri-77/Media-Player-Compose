package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.model.datastore.SongsSortType
import com.example.core.proto_datastore.SortPreferences
import com.example.datastore.serializer.SortPreferencesSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

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

  var songsSortDataStoreManager = SongsSortDataStoreManager(testDataStore, dispatcher)

  @Test
  fun getSongsSortState() = runTest {

    songsSortDataStoreManager.updateSortType(SongsSortType.DURATION)
    songsSortDataStoreManager.updateSortOrder(true)

    var data = songsSortDataStoreManager.sortState.first()

    assertTrue(data.isDec)
    assertEquals(SongsSortType.DURATION, data.sortType)

  }

  @Test
  fun checkUpdateSongsDataStoreMethod() = runTest {

    songsSortDataStoreManager.updateSortType(SongsSortType.DURATION)
    songsSortDataStoreManager.updateSortOrder(true)

    var beforeUpdate = songsSortDataStoreManager.sortState.first()

    assertTrue(beforeUpdate.isDec)
    assertEquals(SongsSortType.DURATION, beforeUpdate.sortType)

    songsSortDataStoreManager.updateSortType(SongsSortType.SIZE)
    songsSortDataStoreManager.updateSortOrder(false)

    var afterUpdate = songsSortDataStoreManager.sortState.first()

    assertFalse(afterUpdate.isDec)
    assertEquals(SongsSortType.SIZE, afterUpdate.sortType)

  }

}
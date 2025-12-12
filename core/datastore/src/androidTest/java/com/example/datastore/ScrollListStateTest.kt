package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.datastore.MyProtoPreferences
import com.example.datastore.serializer.MyProtoPreferencesSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ScrollListStateTest {
    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testContext: Context = ApplicationProvider.getApplicationContext()

    var dispatcher = UnconfinedTestDispatcher()
    var coroutine = CoroutineScope(dispatcher)

    var testDataStore: DataStore<MyProtoPreferences> = DataStoreFactory.create(
        serializer = MyProtoPreferencesSerializer(),
        produceFile = {
            testContext.dataStoreFile(tmpFolder.newFolder().absolutePath)
        },
        scope = coroutine,
    )

    val scrollListDataStoreManager = ScrollListDataStoreManager(dataStore = testDataStore, ioDispatcher = dispatcher)

    @Test
    fun getScrollListState() = runTest {
        scrollListDataStoreManager.updateHomeScroll(1)
        scrollListDataStoreManager.updateAlbumScroll(10)
        scrollListDataStoreManager.updateFolderScroll(10)

        val result = scrollListDataStoreManager.scrollDataStoreState.first()

        assertEquals(1, result.homeMusic)
        assertEquals(10, result.albumMusic)
        assertEquals(0, result.artistMusic)
        assertNotEquals(0, result.folderMusic)
    }
}

package com.example

import com.example.core.data.repository.FavoriteRepository
import com.example.data.dao.FavoriteDaoTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteRepositoryTest {
    private lateinit var favoriteRepository: FavoriteRepository
    private var dummyMediaIds = listOf("1000002", "1000003", "1000004")

    @Before
    fun setup() {
        val dispatcher = UnconfinedTestDispatcher()
        val favoriteDaoTest = FavoriteDaoTest()
        favoriteRepository = FavoriteRepository(favoriteDaoTest, dispatcher)
        addSomeMediaId()

        Dispatchers.setMain(dispatcher)
    }

    private fun addSomeMediaId() {
        runBlocking {
            dummyMediaIds.onEach {
                favoriteRepository.handleFavoriteSongs(it)
            }
        }
    }

    @Test
    fun `get Favorite mediaId list should be 3 item`() = runTest {
        val list = favoriteRepository.favoritesMediaIdList().first()

        assertEquals(3, list.size)
    }

    @Test
    fun `insert new favorite item result should be 4 item`() = runTest {
        val newMediaId = "1000090"
        val list = favoriteRepository.favoritesMediaIdList().first()
        // before add new favorite item should 3 item
        assertEquals(3, list.size)

        favoriteRepository.handleFavoriteSongs(newMediaId)
        val listAfterInsert = favoriteRepository.favoritesMediaIdList().first()
        assertEquals(4, listAfterInsert.size)
    }
}

package com.shermanrex

import com.shermanrex.core.data.repository.FavoriteRepository
import com.shermanrex.core.data.repository.MusicSource
import com.shermanrex.core.data.repository.SearchMusicRepository
import com.shermanrex.data.dao.FavoriteDaoTest
import com.shermanrex.data.repository.MusicRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMusicRepositoryTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val favoriteDaoTest = FavoriteDaoTest()
    private val musicRepositoryFake = MusicRepositoryFake()
    private val favoriteMusicSource = FavoriteRepository(favoriteDao = favoriteDaoTest, ioDispatcher = dispatcher)
    private val musicSource = MusicSource(musicRepositoryFake, favoriteMusicSource, dispatcher)

    var searchMusicRepository = SearchMusicRepository(
        musicSource = musicSource,
        ioDispatcher = dispatcher,
    )

    @Test
    fun `search name should not empty`() = runTest(dispatcher) {
        val searchString = "d"

        searchMusicRepository.search(searchString)

        val resultList = searchMusicRepository.searchList.first()

        // check result size
        assertEquals(3, resultList.size)

        resultList.onEach {
            assertEquals(true, it.name.contains(searchString, true))
        }
    }

    @Test
    fun `search name should return empty`() = runTest(dispatcher) {
        val searchString = "good"

        searchMusicRepository.search(searchString)

        val resultList = searchMusicRepository.searchList.first()

        assertEquals(0, resultList.size)
    }
}

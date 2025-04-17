package com.example

import com.example.core.data.repository.MusicSource
import com.example.core.data.repository.SearchMusicRepository
import com.example.data.repository.MusicRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMusicRepositoryTest {

  var dispatcher = UnconfinedTestDispatcher()
  var musicRepositoryFake = MusicRepositoryFake()
  var musicSource = MusicSource(musicRepositoryFake, dispatcher)

  var searchMusicRepository = SearchMusicRepository(
    musicSource = musicSource,
    ioDispatcher = dispatcher,
  )

  @Test
  fun `search name should not empty`() = runTest(dispatcher) {

    var searchString = "d"

    searchMusicRepository.search(searchString)

    var resultList = searchMusicRepository.searchList.first()

    //check result size
    assertEquals(3, resultList.size)

    resultList.onEach {
      assertEquals(true, it.name.contains(searchString, true))
    }

  }

  @Test
  fun `search name should return empty`() = runTest(dispatcher) {

    var searchString = "good"

    searchMusicRepository.search(searchString)

    var resultList = searchMusicRepository.searchList.first()

    assertEquals(0, resultList.size)
  }


}
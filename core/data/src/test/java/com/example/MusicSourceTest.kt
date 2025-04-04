package com.example

import com.example.core.data.repository.MusicSource
import com.example.data.repository.MusicRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MusicSourceTest {

  private val musicRepositoryFake: MusicRepositoryFake = MusicRepositoryFake()
  private val dispatcher = UnconfinedTestDispatcher()

  private var musicSource = MusicSource(musicRepositoryFake, dispatcher)

  @Test
  fun `get songs list`() = runTest {

    val musicSource = musicSource.songs.first()

    assertEquals(5, musicSource.size)
    assertEquals("Upbeat Pop", musicSource[0].name)
    assertEquals("Ivory Keys", musicSource[1].artist)
    assertEquals("/storage/emulated/0/Music/FunkyGroove.wav", musicSource[2].path)
    assertEquals(10000076, musicSource[4].musicId)
  }

  @Test
  fun `get artist list`() = runTest {

    val musicSource = musicSource.artist().first()

    assertEquals(4, musicSource.size)
    assertEquals("Pop Parade", musicSource[0].categoryName)
    assertEquals("Ivory Keys", musicSource[1].categoryName)
    assertEquals(2, musicSource[1].categoryList.size)
    assertEquals("The Rhythm Kings", musicSource[2].categoryName)
    assertEquals("Quiet Shores", musicSource[3].categoryName)

  }

  @Test
  fun `get album list`() = runTest {

    val musicSource = musicSource.album().first()

    assertEquals(4, musicSource.size)
    assertEquals("Bright Days", musicSource[0].categoryName)
    assertEquals("Silent Moments", musicSource[1].categoryName)
    assertEquals(2, musicSource[3].categoryList.size)
    assertEquals("Dancing Shoes", musicSource[2].categoryName)
    assertEquals("Sunset Dreams", musicSource[3].categoryName)

  }

  @Test
  fun `get folder list`() = runTest {

    val musicSource = musicSource.folder().first()

    assertEquals(2, musicSource.size)
    assertEquals("Music",musicSource[0].categoryName)
    assertEquals(3,musicSource[0].categoryList.size)
    assertEquals("Music 2",musicSource[1].categoryName)





  }

}
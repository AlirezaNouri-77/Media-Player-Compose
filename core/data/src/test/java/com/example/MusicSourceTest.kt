package com.example

import com.example.core.data.repository.MusicSource
import com.example.data.repository.MusicRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MusicSourceTest {
    private val musicRepositoryFake: MusicRepositoryFake = MusicRepositoryFake()
    private val dispatcher = UnconfinedTestDispatcher()

    private var musicSource = MusicSource(musicRepositoryFake, dispatcher)

    @Test
    fun `get songs list`() = runTest {
        val musicSource = musicSource.songs().first()

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
        assertEquals(2, musicSource.find { it.first == "Ivory Keys" }?.second?.size)
        assertNotNull(musicSource.find { it.first == "Pop Parade" })
        assertNotNull(musicSource.find { it.first == "The Rhythm Kings" })
        assertNotNull(musicSource.find { it.first == "Quiet Shores" })
        assertNull(musicSource.find { it.first == "example artist" })
    }

    @Test
    fun `get album list`() = runTest {
        val musicSource = musicSource.album().first()

        assertEquals(4, musicSource.size)
        assertEquals(2, musicSource.find { it.first == "Sunset Dreams" }?.second?.size)
        assertNotNull(musicSource.find { it.first == "Bright Days" })
        assertNotNull(musicSource.find { it.first == "Silent Moments" })
        assertNotNull(musicSource.find { it.first == "Dancing Shoes" })
        assertNotNull(musicSource.find { it.first == "Sunset Dreams" })
        assertNull(musicSource.find { it.first == "example album" })
    }

    @Test
    fun `get folder list`() = runTest {
        val musicSource = musicSource.folder().first()

        assertEquals(2, musicSource.size)
        assertEquals(3, musicSource.find { it.first == "Music" }?.second?.size)
        assertNotNull(musicSource.find { it.first == "Music" })
        assertNotNull(musicSource.find { it.first == "Music 2" })
        assertNull(musicSource.find { it.first == "example folder" })
    }
}

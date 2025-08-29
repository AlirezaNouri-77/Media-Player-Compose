package com.example

import com.example.core.data.repository.FavoriteRepository
import com.example.core.data.repository.MusicSource
import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.data.dao.FavoriteDaoTest
import com.example.data.repository.MusicRepositoryFake
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MusicSourceTest {
    private val musicRepositoryFake: MusicRepositoryFake = MusicRepositoryFake()
    private val favoriteDaoTest = FavoriteDaoTest()
    private lateinit var musicSource: MusicSource
    private lateinit var favoriteRepository: FavoriteRepositoryImpl

    private var dummyMediaIds = listOf("1000002", "1000003", "1000004")

    @Before
    fun setup() {
        val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
        favoriteRepository = FavoriteRepository(favoriteDaoTest, dispatcher)
        musicSource = MusicSource(musicRepositoryFake, favoriteRepository, dispatcher)
        addSomeMediaId()
        Dispatchers.setMain(dispatcher)
    }

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

    private fun addSomeMediaId() {
        runBlocking {
            dummyMediaIds.onEach {
                favoriteRepository.handleFavoriteSongs(it)
            }
        }
    }

    @Test
    fun `get Favorite mediaId list should not be empty`() = runTest {
        val list = musicSource.favorite().first()

        assertEquals(3, list.size)
    }

    @Test
    fun `get Favorite Musics`() = runTest {
        val list = musicSource.favorite().first()

        assertEquals(3, list.size)
    }

    @Test
    fun `check some musics list values`() = runTest {
        val list = musicSource.favorite().first()

        assertEquals(list[0].name, "Upbeat Pop")
        assertEquals(list[0].musicId, 1000002)
        assertEquals(list[2].name, "Funky Groove")
        assertEquals(list[2].musicId, 1000004)
    }

    @Test
    fun `insert favorite song mediaId`() = runTest {
        val newMediaId = "10000010"
        val sizeBeforeAdd = musicSource.favorite().first()

        assertEquals(3, sizeBeforeAdd.size)

        favoriteRepository.handleFavoriteSongs(newMediaId)

        val sizeAfterAdd = musicSource.favorite().first()

        assertEquals(4, sizeAfterAdd.size)
        assertEquals(newMediaId, sizeAfterAdd.find { it.musicId.toString() == newMediaId }?.musicId.toString())
    }

    @Test
    fun `delete favorite song mediaId`() = runTest {
        val newMediaId = "1000002"
        val sizeBeforeAdd = musicSource.favorite().first()

        assertEquals(3, sizeBeforeAdd.size)

        favoriteRepository.handleFavoriteSongs(newMediaId)

        val sizeAfterAdd = musicSource.favorite().first()

        assertEquals(2, sizeAfterAdd.size)
        assertNull(sizeAfterAdd.find { it.musicId.toString() == newMediaId })
    }

    @Test
    fun `get songs after insert new MediaId`() = runTest {
        val newMediaId = "10000076"
        val sizeBeforeAdd = musicSource.favorite().first()

        assertEquals(3, sizeBeforeAdd.size)

        favoriteRepository.handleFavoriteSongs(newMediaId)

        val sizeSonsListAfterAdd = musicSource.favorite().first()

        assertEquals(4, sizeSonsListAfterAdd.size)

        val findNewSong = sizeSonsListAfterAdd.find { it.musicId.toString() == newMediaId }
        assertNotNull(findNewSong)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

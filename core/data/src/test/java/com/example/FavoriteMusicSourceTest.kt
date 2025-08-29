package com.example

import com.example.core.data.repository.FavoriteRepository
import com.example.core.data.repository.MusicSource
import com.example.core.domain.respository.MusicSourceImpl
import com.example.data.dao.FavoriteDaoTest
import com.example.data.repository.MusicRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteMusicSourceTest {
    private lateinit var favoriteMusicSource: FavoriteRepository
    private lateinit var musicSourceTest: MusicSourceImpl
    private lateinit var musicRepositoryFake: MusicRepositoryFake
    private var dummyMediaIds = listOf("1000002", "1000003", "1000004")

    private var dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        musicRepositoryFake = MusicRepositoryFake()
        musicSourceTest = MusicSource(musicRepositoryFake, dispatcher)

        favoriteMusicSource = FavoriteRepository(
            favoriteDao = FavoriteDaoTest(),
            musicSource = musicSourceTest,
            ioDispatcher = dispatcher,
        )

        addSomeMediaId()
    }

    private fun addSomeMediaId() = runTest {
        dummyMediaIds.onEach {
            favoriteMusicSource.handleFavoriteSongs(it)
        }
    }

    @Test
    fun `get Favorite mediaId list should not be empty`() = runTest {
        val list = favoriteMusicSource.favoriteMusicMediaIdList.first()

        assertEquals(3, list.size)
    }

    @Test
    fun `get Favorite Musics`() = runTest(dispatcher) {
        val list = favoriteMusicSource.favoriteSongs.first()

        assertEquals(3, list.size)
    }

    @Test
    fun `check some musics list values`() = runTest(dispatcher) {
        val list = favoriteMusicSource.favoriteSongs.first()

        assertEquals(list[0].name, "Upbeat Pop")
        assertEquals(list[0].musicId, 1000002)
        assertEquals(list[2].name, "Funky Groove")
        assertEquals(list[2].musicId, 1000004)
    }

    @Test
    fun `insert favorite song mediaId`() = runTest {
        val newMediaId = "10000010"
        val sizeBeforeAdd = favoriteMusicSource.favoriteMusicMediaIdList.first()

        assertEquals(3, sizeBeforeAdd.size)

        favoriteMusicSource.handleFavoriteSongs(newMediaId)

        val sizeAfterAdd = favoriteMusicSource.favoriteMusicMediaIdList.first()

        assertEquals(4, sizeAfterAdd.size)
        assertEquals(newMediaId, sizeAfterAdd.find { it == newMediaId })
    }

    @Test
    fun `delete favorite song mediaId`() = runTest {
        val newMediaId = "1000002"
        val sizeBeforeAdd = favoriteMusicSource.favoriteMusicMediaIdList.first()

        assertEquals(3, sizeBeforeAdd.size)

        favoriteMusicSource.handleFavoriteSongs(newMediaId)

        val sizeAfterAdd = favoriteMusicSource.favoriteMusicMediaIdList.first()

        assertEquals(2, sizeAfterAdd.size)
        assertNull(sizeAfterAdd.find { it == newMediaId })
    }

    @Test
    fun `get songs after insert new MediaId`() = runTest(dispatcher) {
        val newMediaId = "10000076"
        val sizeBeforeAdd = favoriteMusicSource.favoriteMusicMediaIdList.first()

        assertEquals(3, sizeBeforeAdd.size)

        favoriteMusicSource.handleFavoriteSongs(newMediaId)

        val sizeSonsListAfterAdd = favoriteMusicSource.favoriteSongs.first()

        assertEquals(4, sizeSonsListAfterAdd.size)

        val findNewSong = sizeSonsListAfterAdd.find { it.musicId.toString() == newMediaId }
        assertNotNull(findNewSong)
    }
}

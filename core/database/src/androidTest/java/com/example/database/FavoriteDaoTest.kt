package com.example.database

import com.example.core.database.model.FavoriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FavoriteDaoTest : DataBaseTest() {
    private val testFavoriteMediaId = listOf(
        FavoriteEntity(mediaId = "mediaId-1"),
        FavoriteEntity(mediaId = "mediaId-2"),
        FavoriteEntity(mediaId = "mediaId-3"),
        FavoriteEntity(mediaId = "mediaId-4"),
    )

    @Test
    fun getFavorites() = runTest {
        testFavoriteMediaId.onEach {
            favoriteDao.insertFavoriteSong(it)
        }

        val favoriteList = favoriteDao.getFavoriteSongsMediaId().first()
        val expectedList = testFavoriteMediaId.map { it.mediaId }

        assertEquals(expectedList, favoriteList)
    }

    @Test
    fun isMediaIdInFavoriteDatabase() = runTest {
        val mediaId = "mediaId-1"

        testFavoriteMediaId.onEach {
            favoriteDao.insertFavoriteSong(it)
        }

        val isFavorite = favoriteDao.isFavorite(mediaId)
        assertEquals(true, isFavorite)
    }

    @Test
    fun isMediaIdNotInFavoriteDatabase() = runTest {
        val mediaId = "mediaId-5"

        testFavoriteMediaId.onEach {
            favoriteDao.insertFavoriteSong(it)
        }

        val isFavorite = favoriteDao.isFavorite(mediaId)
        assertEquals(false, isFavorite)
    }

    @Test
    fun insertMediaIdSuccess() = runTest {
        val mediaId = FavoriteEntity(mediaId = "mediaId-1")

        favoriteDao.insertFavoriteSong(mediaId)

        val favoriteList = favoriteDao.getFavoriteSongsMediaId().first()

        assertEquals(mediaId.mediaId, favoriteList.first())
    }

    @Test
    fun deleteMediaId() = runTest {
        val targetMediaId = "mediaId-1"

        testFavoriteMediaId.onEach {
            favoriteDao.insertFavoriteSong(it)
        }

        val firstQuery = favoriteDao.getFavoriteSongsMediaId().first()

        assertEquals(true, firstQuery.contains(targetMediaId))

        favoriteDao.deleteFavoriteSong(targetMediaId)

        val secondQuery = favoriteDao.getFavoriteSongsMediaId().first()

        assertEquals(false, secondQuery.contains(targetMediaId))
    }
}

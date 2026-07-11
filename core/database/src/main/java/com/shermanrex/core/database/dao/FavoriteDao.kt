package com.shermanrex.core.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.shermanrex.core.database.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT media_Id FROM FavoriteEntity")
    fun getFavoriteSongsMediaId(): Flow<List<String>>

    @Insert
    fun insertFavoriteSong(favoriteMediaId: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE media_Id = :mediaId")
    fun deleteFavoriteSong(mediaId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM FavoriteEntity WHERE media_Id=:mediaId) ")
    fun isFavorite(mediaId: String): Boolean
}

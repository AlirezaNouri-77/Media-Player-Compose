package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.core.database.model.FavoriteEntity
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

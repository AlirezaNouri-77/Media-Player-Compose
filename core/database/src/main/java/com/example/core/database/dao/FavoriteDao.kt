package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

  @Query("SELECT media_Id FROM FavoriteEntity")
  fun getFavoriteSongsMediaId(): Flow<List<String>>

  @androidx.room.Insert
  fun insertFavoriteSong(favoriteMediaId: com.example.core.database.model.FavoriteEntity)

  @Query("DELETE FROM FavoriteEntity WHERE media_Id = :mediaId")
  fun deleteFavoriteSong(mediaId: String)

  @Query("SELECT EXISTS(SELECT 1 FROM FavoriteEntity WHERE media_Id=:mediaId) ")
  fun isFavorite(mediaId: String): Boolean
}
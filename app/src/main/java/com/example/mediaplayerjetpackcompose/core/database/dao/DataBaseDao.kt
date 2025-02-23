package com.example.mediaplayerjetpackcompose.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mediaplayerjetpackcompose.core.model.FavoriteMusic
import kotlinx.coroutines.flow.Flow

@Dao
interface DataBaseDao {

  @Query("SELECT media_Id FROM favoritemusic")
  fun getFavoriteSongsMediaId(): Flow<List<String>>

  @Insert
  fun insertFavoriteSong(favoriteMediaId: FavoriteMusic)

  @Query("DELETE FROM favoritemusic WHERE media_Id = :mediaId")
  fun deleteFavoriteSong(mediaId: String)

  @Query("SELECT EXISTS(SELECT 1 FROM favoritemusic WHERE media_Id=:mediaId) ")
  fun isFavorite(mediaId: String): Boolean
}
package com.example.mediaplayerjetpackcompose.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.FavoriteMusicModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DataBaseDao {

  @Query("SELECT media_Id FROM favoritemusicmodel")
  fun getFavoriteSongsMediaId(): Flow<List<String>>

  @Insert
  fun insertFavoriteSong(favoriteMediaId: FavoriteMusicModel)

  @Query("DELETE FROM favoritemusicmodel WHERE media_Id = :mediaId")
  fun deleteFavoriteSong(mediaId: String)

  @Query("SELECT EXISTS(SELECT 1 FROM favoritemusicmodel WHERE media_Id=:mediaId) ")
  fun isFavorite(mediaId: String): Boolean
}
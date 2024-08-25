package com.example.mediaplayerjetpackcompose.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.FavoriteMusicModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DataBaseDao {

  @Query("SELECT * FROM favoritemusicmodel")
  fun getAllFaFavoriteSongs(): Flow<List<FavoriteMusicModel>>

  @Insert
  fun insertFavoriteSong(favoriteMediaId: FavoriteMusicModel)

  @Query("DELETE FROM favoritemusicmodel WHERE media_Id = :mediaId")
  fun deleteFavoriteSong(mediaId: String)

}
package com.example.mediaplayerjetpackcompose.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mediaplayerjetpackcompose.domain.model.FavoriteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DataBaseDao {

  @Query("SELECT * FROM favoritemodel")
  fun getAllFaFavoriteSongs(): Flow<List<FavoriteModel>>

  @Insert
  fun insertFavoriteSong(favoriteMediaId: FavoriteModel)

  @Query("DELETE FROM favoritemodel WHERE media_Id = :mediaId")
  fun deleteFavoriteSong(mediaId: String)

}
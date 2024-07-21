package com.example.mediaplayerjetpackcompose.domain.model.musicScreen

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMusicModel(
  @PrimaryKey(autoGenerate = true) var id: Int = 0,
  @ColumnInfo(name = "media_Id") var mediaId: String,
)
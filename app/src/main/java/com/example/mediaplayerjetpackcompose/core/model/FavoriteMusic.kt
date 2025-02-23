package com.example.mediaplayerjetpackcompose.core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMusic(
  @PrimaryKey(autoGenerate = true) var id: Int = 0,
  @ColumnInfo(name = "media_Id") var mediaId: String,
)
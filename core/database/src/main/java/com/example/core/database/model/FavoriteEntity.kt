package com.example.core.database.model

@androidx.room.Entity
data class FavoriteEntity(
  @androidx.room.PrimaryKey(autoGenerate = true) var id: Int = 0,
  @androidx.room.ColumnInfo(name = "media_Id") var mediaId: String,
)
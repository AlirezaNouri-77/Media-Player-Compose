package com.example.mediaplayerjetpackcompose.data.database.databaseClass

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mediaplayerjetpackcompose.data.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.FavoriteMusicModel

@Database(entities = [FavoriteMusicModel::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

  abstract fun dataBaseDao(): DataBaseDao

}
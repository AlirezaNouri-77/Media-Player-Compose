package com.example.mediaplayerjetpackcompose.core.database.databaseClass

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mediaplayerjetpackcompose.core.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.core.model.FavoriteMusic

@Database(entities = [FavoriteMusic::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

  abstract fun dataBaseDao(): DataBaseDao

}
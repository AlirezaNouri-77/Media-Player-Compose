package com.example.mediaplayerjetpackcompose.data.database.databaseClass

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mediaplayerjetpackcompose.data.database.dao.DataBaseDao
import com.example.mediaplayerjetpackcompose.domain.model.FavoriteModel

@Database(entities = [FavoriteModel::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

  abstract fun dataBaseDao(): DataBaseDao

  companion object {
    @Volatile
    var INSTANCE: AppDataBase? = null

    private fun getInstance(context: Context): AppDataBase {
      return INSTANCE ?: synchronized(context) {
        INSTANCE ?: getAppDataBase(context)
      }
    }

    fun getAppDataBase(context: Context): AppDataBase = Room.databaseBuilder(
      context = context,
      AppDataBase::class.java, "DataBase_MediaPlayer",
    ).build()
  }

}
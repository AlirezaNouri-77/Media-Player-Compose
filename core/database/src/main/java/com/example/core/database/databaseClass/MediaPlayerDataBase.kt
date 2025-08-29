package com.example.core.database.databaseClass

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.database.dao.FavoriteDao
import com.example.core.database.model.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class MediaPlayerDataBase : RoomDatabase() {
    abstract fun dataBaseDao(): FavoriteDao
}

package com.shermanrex.core.database.databaseClass

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.shermanrex.core.database.dao.FavoriteDao
import com.shermanrex.core.database.model.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class MediaPlayerDataBase : RoomDatabase() {
    abstract fun dataBaseDao(): FavoriteDao
}

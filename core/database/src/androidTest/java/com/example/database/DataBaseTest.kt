package com.example.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.dao.FavoriteDao
import com.example.core.database.databaseClass.MediaPlayerDataBase
import org.junit.After
import org.junit.Before

internal abstract class DataBaseTest {
    private lateinit var database: MediaPlayerDataBase
    lateinit var favoriteDao: FavoriteDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        database = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = MediaPlayerDataBase::class.java,
        ).build()

        favoriteDao = database.dataBaseDao()
    }

    @After
    fun clean() = database.close()
}

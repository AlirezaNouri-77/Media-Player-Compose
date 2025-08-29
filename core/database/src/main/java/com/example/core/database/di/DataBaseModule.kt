package com.example.core.database.di

import androidx.room.Room
import com.example.core.database.databaseClass.MediaPlayerDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

var DataBaseModule = module {

    single { get<MediaPlayerDataBase>().dataBaseDao() }

    single {
        Room.databaseBuilder(
            context = androidApplication().applicationContext,
            MediaPlayerDataBase::class.java,
            "DataBase_MediaPlayer",
        ).build()
    }
}

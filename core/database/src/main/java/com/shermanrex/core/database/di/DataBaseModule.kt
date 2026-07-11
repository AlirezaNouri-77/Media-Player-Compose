package com.shermanrex.core.database.di

import androidx.room3.Room
import com.shermanrex.core.database.databaseClass.MediaPlayerDataBase
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

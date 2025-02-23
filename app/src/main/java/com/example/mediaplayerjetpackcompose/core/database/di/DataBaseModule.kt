package com.example.mediaplayerjetpackcompose.core.database.di

import androidx.room.Room
import com.example.mediaplayerjetpackcompose.core.database.databaseClass.AppDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

var DataBaseModule = module {

  single {
    Room.databaseBuilder(
      context = androidApplication().applicationContext,
      AppDataBase::class.java, "DataBase_MediaPlayer",
    ).build()
  }

}
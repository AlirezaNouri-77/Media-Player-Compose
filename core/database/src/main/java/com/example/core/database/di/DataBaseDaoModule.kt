package com.example.core.database.di

import com.example.core.database.databaseClass.MediaPlayerDataBase
import org.koin.dsl.module

var DataBaseDaoModule = module {

  single { get<MediaPlayerDataBase>().dataBaseDao() }

}
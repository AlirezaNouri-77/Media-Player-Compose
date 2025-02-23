package com.example.mediaplayerjetpackcompose.core.database.di

import com.example.mediaplayerjetpackcompose.core.database.databaseClass.AppDataBase
import org.koin.dsl.module

var DataBaseDaoModule = module {

  single { get<AppDataBase>().dataBaseDao() }

}
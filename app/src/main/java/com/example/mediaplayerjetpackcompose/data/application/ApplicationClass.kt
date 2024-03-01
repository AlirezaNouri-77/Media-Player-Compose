package com.example.mediaplayerjetpackcompose.data.application

import android.app.Application
import com.example.mediaplayerjetpackcompose.data.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ApplicationClass : Application() {

  override fun onCreate() {
    super.onCreate()
    startKoin {
      allowOverride(true)
      androidContext(this@ApplicationClass)
      androidLogger(Level.INFO)
      modules(appModule)
    }
  }
}